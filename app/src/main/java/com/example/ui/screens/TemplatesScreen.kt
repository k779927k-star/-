package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.db.FlowNodeEntity
import com.example.ui.components.BackgroundWrapper
import com.example.ui.components.FreeBadge
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.FreeGreen
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.RadiantAmber
import com.example.ui.viewmodel.FlowViewModel

data class TemplatePreset(
    val id: String,
    val name: String,
    val category: String,
    val description: String,
    val icon: ImageVector,
    val color: Color,
    val nodes: List<FlowNodeEntity>
)

@Composable
fun TemplatesScreen(
    viewModel: FlowViewModel,
    onNavigateBack: () -> Unit,
    onProjectCreated: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val templates = listOf(
        TemplatePreset(
            id = "t_android",
            name = "مطور تطبيقات Android Jetpack Compose",
            category = "برمجيات",
            description = "تدفق احترافي يولد هيكل الشاشات، كود الـ Composable، إدارة الحالة بالـ ViewModel واختبارات الوحدة.",
            icon = Icons.Default.Code,
            color = CyberCyan,
            nodes = listOf(
                FlowNodeEntity(projectId = "", stepOrder = 1, title = "1. متطلبات شاشات التطبيق", nodeType = "INPUT", promptTemplate = "تطبيق متجر إلكتروني ذكي للمنتجات الرقمية مع سلة مشتريات ودفع إلكتروني."),
                FlowNodeEntity(projectId = "", stepOrder = 2, title = "2. هيكلة طبقة البيانات والـ State", nodeType = "PROMPT", promptTemplate = "صمم نموذج البيانات StateFlow والواجهة البرمجية Repository."),
                FlowNodeEntity(projectId = "", stepOrder = 3, title = "3. كود واجهات Compose", nodeType = "CODE_GEN", promptTemplate = "اكتب كود Jetpack Compose كامل ومتقن للشاشة الرئيسية مع بطاقات المنتجات.")
            )
        ),
        TemplatePreset(
            id = "t_marketing",
            name = "صانع استراتيجيات المحتوى والتسويق",
            category = "محتوى",
            description = "خط أنابيب ذكي لصياغة خطة تسويقية، منشورات إعلانية جذابة، ونصوص فيديو تسويقي.",
            icon = Icons.Default.AutoAwesome,
            color = NeonPurple,
            nodes = listOf(
                FlowNodeEntity(projectId = "", stepOrder = 1, title = "1. تحليل المنتج والجمهور المستهدف", nodeType = "INPUT", promptTemplate = "تطبيق KALED - منصة ذكاء اصطناعي ومشاريع مجانية بالكامل للمطورين وصناع المحتوى."),
                FlowNodeEntity(projectId = "", stepOrder = 2, title = "2. توليد خطة الحملة الإعلانية", nodeType = "PROMPT", promptTemplate = "اقترح 5 أفكار رئيسية لحملة إطلاق التطبيق مع خطة نشر أسبوعية."),
                FlowNodeEntity(projectId = "", stepOrder = 3, title = "3. صياغة المنشورات والوسوم", nodeType = "OUTPUT", promptTemplate = "اكتب 3 منشورات مشوقة مع وسوم Hashtags ودعوة واضحة لتجربة التطبيق مجاناً.")
            )
        ),
        TemplatePreset(
            id = "t_startup",
            name = "محلل الشركات الناشئة والأفكار الاستثمارية",
            category = "أعمال",
            description = "تفكيك فكرة المشروع إلى نموذج عمل تجاري (Business Canvas)، دراسة الجدوى، وخطة التوسع.",
            icon = Icons.Default.Lightbulb,
            color = RadiantAmber,
            nodes = listOf(
                FlowNodeEntity(projectId = "", stepOrder = 1, title = "1. وصف فكرة الشركة الناشئة", nodeType = "INPUT", promptTemplate = "منصة سحابية متخصصة في أتمتة مهام الذكاء الاصطناعي للمتاجر الإلكترونية العربية."),
                FlowNodeEntity(projectId = "", stepOrder = 2, title = "2. صياغة نموذج العمل Canvas", nodeType = "PROMPT", promptTemplate = "حدد مصادر الدخل، شرائح العملاء، القيمة المضافة المقترحة وقنوات الوصول."),
                FlowNodeEntity(projectId = "", stepOrder = 3, title = "3. عرض المشروع للمستثمرين (Pitch Deck)", nodeType = "OUTPUT", promptTemplate = "اكتب هيكل عرض تقديمي من 7 شرائح رئيسية لإقناع المستثمرين بالتمويل الأولي.")
            )
        ),
        TemplatePreset(
            id = "t_research",
            name = "محلل الأبحاث والتلخيص العميق",
            category = "بحث وتحليل",
            description = "تحويل المواضيع المعقدة والمقالات التقنية إلى ملخصات ذكية ونقاط عمل تنفيذية.",
            icon = Icons.Default.Search,
            color = FreeGreen,
            nodes = listOf(
                FlowNodeEntity(projectId = "", stepOrder = 1, title = "1. موضوع البحث أو النص الأصلي", nodeType = "INPUT", promptTemplate = "التطورات الأخيرة في نماذج الاستدلال المنطقي ونماذج Gemini 3.5 الفائقة."),
                FlowNodeEntity(projectId = "", stepOrder = 2, title = "2. استخراج النقاط الجوهرية والنتائج", nodeType = "PROMPT", promptTemplate = "قم بتلخيص أهم 5 ابتكارات تقنية مع شرح أثرها العملي على تطوير التطبيقات."),
                FlowNodeEntity(projectId = "", stepOrder = 3, title = "3. التوصيات والخطوات العملية", nodeType = "OUTPUT", promptTemplate = "صِغ قائمة خطوات تنفيذية واضحة للمطورين للاستفادة من هذه التقنيات فوراً.")
            )
        )
    )

    BackgroundWrapper {
        Box(modifier = modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("templates_screen_list"),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 40.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = onNavigateBack,
                                modifier = Modifier.testTag("templates_back_button")
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "رجوع",
                                    tint = Color.White
                                )
                            }
                            Spacer(modifier = Modifier.width(4.dp))
                            Column {
                                Text(
                                    text = "قوالب تدفقات KALED الجاهزة",
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = "استنسخ أي تدفق إلى مشاريعك مجاناً بنقرة واحدة",
                                    fontSize = 11.sp,
                                    color = CyberCyan
                                )
                            }
                        }

                        FreeBadge(text = "قوالب مجانية")
                    }
                }

                // Templates List
                items(templates, key = { it.id }) { template ->
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .border(
                                1.dp,
                                Brush.horizontalGradient(listOf(template.color.copy(alpha = 0.6f), CyberCyan.copy(alpha = 0.3f))),
                                RoundedCornerShape(20.dp)
                            ),
                        color = Color(0xF00D1526)
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(38.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(template.color.copy(alpha = 0.2f))
                                            .border(1.dp, template.color, RoundedCornerShape(10.dp)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = template.icon,
                                            contentDescription = null,
                                            tint = template.color,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = template.name,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                        Text(
                                            text = "${template.category} • ${template.nodes.size} خطوات ذكية",
                                            fontSize = 11.sp,
                                            color = template.color
                                        )
                                    }
                                }

                                Text(
                                    text = "مجاني ⚡",
                                    fontSize = 11.sp,
                                    color = FreeGreen,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = template.description,
                                fontSize = 12.sp,
                                color = Color(0xFFCBD5E1),
                                lineHeight = 18.sp
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            Button(
                                onClick = {
                                    viewModel.createProject(
                                        name = template.name,
                                        description = template.description,
                                        category = template.category,
                                        nodes = template.nodes
                                    )
                                    Toast.makeText(context, "تم استنساخ المشروع في حسابك بنجاح مجاناً 🚀", Toast.LENGTH_SHORT).show()
                                    onProjectCreated()
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(44.dp)
                                    .testTag("clone_template_${template.id}"),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = CyberCyan,
                                    contentColor = Color.Black
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ContentCopy,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "استخدام هذا التدفق مجاناً ⚡",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
