package com.example.ui.components

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Input
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.db.FlowNodeEntity
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.FreeGreen
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.NodeCodeGenColor
import com.example.ui.theme.NodeInputColor
import com.example.ui.theme.NodeOutputColor
import com.example.ui.theme.NodePromptColor

@Composable
fun FlowNodeCard(
    node: FlowNodeEntity,
    isLastNode: Boolean,
    onExecuteNode: () -> Unit,
    onUpdateNodePrompt: (String) -> Unit,
    onDeleteNode: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    var isEditingPrompt by remember { mutableStateOf(false) }
    var promptText by remember(node.promptTemplate) { mutableStateOf(node.promptTemplate) }

    val nodeColor = when (node.nodeType) {
        "INPUT" -> NodeInputColor
        "CODE_GEN" -> NodeCodeGenColor
        "OUTPUT" -> NodeOutputColor
        else -> NodePromptColor
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Main Node Card
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .border(
                    width = 1.2.dp,
                    brush = Brush.linearGradient(
                        listOf(
                            nodeColor.copy(alpha = 0.8f),
                            CyberCyan.copy(alpha = 0.4f)
                        )
                    ),
                    shape = RoundedCornerShape(18.dp)
                )
                .testTag("node_card_${node.id}"),
            color = Color(0xF0101828)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                // Header Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Step Index Badge
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(nodeColor.copy(alpha = 0.25f))
                                .border(1.dp, nodeColor, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${node.stepOrder}",
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 13.sp
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column {
                            Text(
                                text = node.title,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 15.sp,
                                color = Color(0xFFF1F5F9)
                            )
                            Text(
                                text = when (node.nodeType) {
                                    "INPUT" -> "مدخل وسياق التدفق"
                                    "CODE_GEN" -> "توليد كود برمجي"
                                    "OUTPUT" -> "صياغة المخرجات النهائية"
                                    else -> "معالجة واستنتاج AI"
                                },
                                fontSize = 11.sp,
                                color = nodeColor
                            )
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = { isEditingPrompt = !isEditingPrompt },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "تعديل المطلب",
                                tint = Color(0xFF94A3B8),
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        IconButton(
                            onClick = onDeleteNode,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "حذف العقدة",
                                tint = Color(0xFFEF4444),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Prompt content
                if (isEditingPrompt) {
                    OutlinedTextField(
                        value = promptText,
                        onValueChange = { promptText = it },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyberCyan,
                            unfocusedBorderColor = Color(0xFF334155),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color(0xFFE2E8F0)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(
                            onClick = {
                                onUpdateNodePrompt(promptText)
                                isEditingPrompt = false
                            }
                        ) {
                            Text("حفظ التعديل", color = CyberCyan, fontWeight = FontWeight.Bold)
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF1E293B).copy(alpha = 0.6f))
                            .padding(12.dp)
                    ) {
                        Text(
                            text = node.promptTemplate.ifBlank { "لا يوجد نص أو تعليمات لهذه الخطوة" },
                            fontSize = 13.sp,
                            color = Color(0xFFCBD5E1),
                            lineHeight = 18.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Action & Status bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Status indicator
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        when (node.status) {
                            "RUNNING" -> {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    strokeWidth = 2.dp,
                                    color = CyberCyan
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "جاري المعالجة بالذكاء الاصطناعي...",
                                    fontSize = 11.sp,
                                    color = CyberCyan
                                )
                            }
                            "COMPLETED" -> {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = FreeGreen,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "تم التنفيذ بنجاح",
                                    fontSize = 11.sp,
                                    color = FreeGreen
                                )
                            }
                            "ERROR" -> {
                                Icon(
                                    imageVector = Icons.Default.Error,
                                    contentDescription = null,
                                    tint = Color(0xFFEF4444),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "فشل في التنفيذ",
                                    fontSize = 11.sp,
                                    color = Color(0xFFEF4444)
                                )
                            }
                            else -> {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF64748B))
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "في انتظار التشغيل",
                                    fontSize = 11.sp,
                                    color = Color(0xFF94A3B8)
                                )
                            }
                        }
                    }

                    // Run Step Button
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                Brush.horizontalGradient(
                                    listOf(CyberCyan.copy(alpha = 0.2f), NeonPurple.copy(alpha = 0.2f))
                                )
                            )
                            .border(1.dp, CyberCyan.copy(alpha = 0.6f), RoundedCornerShape(10.dp))
                    ) {
                        TextButton(
                            onClick = onExecuteNode,
                            enabled = node.status != "RUNNING",
                            modifier = Modifier.testTag("run_node_button_${node.id}")
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = null,
                                tint = CyberCyan,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "تشغيل هذه الخطوة",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = CyberCyan
                            )
                        }
                    }
                }

                // Output Container
                AnimatedVisibility(visible = node.latestOutput.isNotBlank()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 14.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF0B132B))
                            .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(12.dp))
                            .padding(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    tint = CyberCyan,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "مخرجات الذكاء الاصطناعي:",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CyberCyan
                                )
                            }

                            IconButton(
                                onClick = {
                                    clipboardManager.setText(AnnotatedString(node.latestOutput))
                                    Toast.makeText(context, "تم نسخ المخرجات بنجاح", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ContentCopy,
                                    contentDescription = "نسخ",
                                    tint = Color(0xFF94A3B8),
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = node.latestOutput,
                            fontSize = 12.sp,
                            color = Color(0xFFE2E8F0),
                            fontFamily = if (node.nodeType == "CODE_GEN") FontFamily.Monospace else FontFamily.Default,
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }

        // Connecting Flow Line between nodes
        if (!isLastNode) {
            Box(
                modifier = Modifier
                    .width(3.dp)
                    .height(28.dp)
                    .background(
                        Brush.verticalGradient(
                            listOf(nodeColor.copy(alpha = 0.8f), CyberCyan.copy(alpha = 0.8f))
                        )
                    )
            )
            Icon(
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = null,
                tint = CyberCyan,
                modifier = Modifier
                    .size(14.dp)
                    .padding(vertical = 2.dp)
            )
            Box(
                modifier = Modifier
                    .width(3.dp)
                    .height(8.dp)
                    .background(CyberCyan)
            )
        }
    }
}
