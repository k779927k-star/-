package com.example.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface FlowDao {
    @Query("SELECT * FROM projects ORDER BY updatedAt DESC")
    fun getAllProjects(): Flow<List<FlowProjectEntity>>

    @Query("SELECT * FROM projects WHERE id = :id LIMIT 1")
    suspend fun getProjectById(id: String): FlowProjectEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProject(project: FlowProjectEntity)

    @Update
    suspend fun updateProject(project: FlowProjectEntity)

    @Delete
    suspend fun deleteProject(project: FlowProjectEntity)

    // Flow Nodes
    @Query("SELECT * FROM flow_nodes WHERE projectId = :projectId ORDER BY stepOrder ASC")
    fun getNodesForProject(projectId: String): Flow<List<FlowNodeEntity>>

    @Query("SELECT * FROM flow_nodes WHERE projectId = :projectId ORDER BY stepOrder ASC")
    suspend fun getNodesListForProject(projectId: String): List<FlowNodeEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNode(node: FlowNodeEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNodes(nodes: List<FlowNodeEntity>)

    @Update
    suspend fun updateNode(node: FlowNodeEntity)

    @Delete
    suspend fun deleteNode(node: FlowNodeEntity)

    @Query("DELETE FROM flow_nodes WHERE projectId = :projectId")
    suspend fun deleteNodesForProject(projectId: String)

    // Chat
    @Query("SELECT * FROM chat_messages ORDER BY timestamp ASC")
    fun getAllChatMessages(): Flow<List<ChatMessageEntity>>

    @Query("SELECT * FROM chat_messages ORDER BY timestamp ASC")
    suspend fun getAllChatMessagesList(): List<ChatMessageEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChatMessage(message: ChatMessageEntity)

    @Query("DELETE FROM chat_messages")
    suspend fun clearChatMessages()
}
