package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.db.AppDatabase
import com.example.data.db.ChatMessageEntity
import com.example.data.db.FlowNodeEntity
import com.example.data.db.FlowProjectEntity
import com.example.data.repository.FlowRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FlowViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: FlowRepository

    private val _selectedCategoryFilter = MutableStateFlow("الكل")
    val selectedCategoryFilter: StateFlow<String> = _selectedCategoryFilter.asStateFlow()

    private val _selectedProject = MutableStateFlow<FlowProjectEntity?>(null)
    val selectedProject: StateFlow<FlowProjectEntity?> = _selectedProject.asStateFlow()

    private val _projectNodes = MutableStateFlow<List<FlowNodeEntity>>(emptyList())
    val projectNodes: StateFlow<List<FlowNodeEntity>> = _projectNodes.asStateFlow()

    private val _isFlowExecuting = MutableStateFlow(false)
    val isFlowExecuting: StateFlow<Boolean> = _isFlowExecuting.asStateFlow()

    private val _isChatSending = MutableStateFlow(false)
    val isChatSending: StateFlow<Boolean> = _isChatSending.asStateFlow()

    val allProjects: StateFlow<List<FlowProjectEntity>>
    val filteredProjects: StateFlow<List<FlowProjectEntity>>
    val chatMessages: StateFlow<List<ChatMessageEntity>>

    init {
        val db = AppDatabase.getDatabase(application)
        repository = FlowRepository(db.flowDao())

        allProjects = repository.allProjects
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

        chatMessages = repository.allChatMessages
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

        filteredProjects = combine(allProjects, _selectedCategoryFilter) { projects, filter ->
            when (filter) {
                "الكل" -> projects
                "المفضلة" -> projects.filter { it.isFavorite }
                else -> projects.filter { it.category == filter }
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    }

    fun setCategoryFilter(category: String) {
        _selectedCategoryFilter.value = category
    }

    fun selectProject(project: FlowProjectEntity?) {
        _selectedProject.value = project
        if (project != null) {
            viewModelScope.launch {
                repository.getNodesForProject(project.id).collect { nodes ->
                    _projectNodes.value = nodes
                }
            }
        } else {
            _projectNodes.value = emptyList()
        }
    }

    fun createProject(name: String, description: String, category: String, nodes: List<FlowNodeEntity>) {
        viewModelScope.launch {
            repository.createProject(name, description, category, nodes)
        }
    }

    fun updateProject(project: FlowProjectEntity) {
        viewModelScope.launch {
            repository.updateProject(project)
        }
    }

    fun deleteProject(project: FlowProjectEntity) {
        viewModelScope.launch {
            if (_selectedProject.value?.id == project.id) {
                _selectedProject.value = null
                _projectNodes.value = emptyList()
            }
            repository.deleteProject(project)
        }
    }

    fun toggleFavorite(project: FlowProjectEntity) {
        viewModelScope.launch {
            repository.updateProject(project.copy(isFavorite = !project.isFavorite))
        }
    }

    fun addNode(project: FlowProjectEntity, title: String, prompt: String, nodeType: String) {
        viewModelScope.launch {
            val nextOrder = (_projectNodes.value.maxOfOrNull { it.stepOrder } ?: 0) + 1
            val newNode = FlowNodeEntity(
                projectId = project.id,
                stepOrder = nextOrder,
                title = title,
                nodeType = nodeType,
                promptTemplate = prompt
            )
            repository.insertNode(newNode)
        }
    }

    fun updateNodePrompt(node: FlowNodeEntity, newPrompt: String) {
        viewModelScope.launch {
            repository.updateNode(node.copy(promptTemplate = newPrompt))
        }
    }

    fun deleteNode(node: FlowNodeEntity) {
        viewModelScope.launch {
            repository.deleteNode(node)
        }
    }

    fun executeNode(node: FlowNodeEntity, project: FlowProjectEntity) {
        viewModelScope.launch {
            val nodes = _projectNodes.value
            val previousNode = nodes.filter { it.stepOrder < node.stepOrder }.maxByOrNull { it.stepOrder }
            val previousOutput = previousNode?.latestOutput ?: ""
            repository.executeNode(node, previousOutput, project.systemPrompt)
        }
    }

    fun executeEntireFlow(project: FlowProjectEntity) {
        viewModelScope.launch {
            _isFlowExecuting.value = true
            try {
                repository.executeEntireFlow(project.id, project.systemPrompt)
            } finally {
                _isFlowExecuting.value = false
            }
        }
    }

    fun sendChatMessage(text: String) {
        if (text.isBlank()) return
        viewModelScope.launch {
            _isChatSending.value = true
            try {
                repository.sendChatMessage(text)
            } finally {
                _isChatSending.value = false
            }
        }
    }

    fun clearChat() {
        viewModelScope.launch {
            repository.clearChat()
        }
    }
}
