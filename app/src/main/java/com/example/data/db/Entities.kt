package com.example.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "projects")
data class FlowProjectEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val description: String,
    val category: String,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val isFavorite: Boolean = false,
    val systemPrompt: String = ""
)

@Entity(tableName = "flow_nodes")
data class FlowNodeEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val projectId: String,
    val stepOrder: Int,
    val title: String,
    val nodeType: String = "PROMPT", // INPUT, PROMPT, CODE_GEN, REFINER, OUTPUT
    val promptTemplate: String = "",
    val latestOutput: String = "",
    val status: String = "IDLE" // IDLE, RUNNING, COMPLETED, ERROR
)

@Entity(tableName = "chat_messages")
data class ChatMessageEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val projectId: String? = null,
    val role: String, // "user" or "model"
    val content: String,
    val timestamp: Long = System.currentTimeMillis()
)
