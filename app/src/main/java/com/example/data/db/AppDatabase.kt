package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID

@Database(
    entities = [FlowProjectEntity::class, FlowNodeEntity::class, ChatMessageEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun flowDao(): FlowDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "kaled_flow_database"
                )
                    .addCallback(DatabaseCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        populateInitialFreeProjects(database.flowDao())
                    }
                }
            }

            private suspend fun populateInitialFreeProjects(dao: FlowDao) {
                // Project 1: Android Kotlin Compose Flow
                val p1Id = UUID.randomUUID().toString()
                val p1 = FlowProjectEntity(
                    id = p1Id,
                    name = "مطور تطبيقات أندرويد (Android Jetpack Flow)",
                    description = "تدفق ذكي متكامل لتوليد بنية وتصميم وأكواد تطبيقات أندرويد بالـ Compose مع اختبارات.",
                    category = "برمجيات",
                    systemPrompt = "أنت خبير في تطوير تطبيقات الأندرويد باستخدام Kotlin و Jetpack Compose."
                )
                dao.insertProject(p1)
                dao.insertNodes(
                    listOf(
                        FlowNodeEntity(
                            projectId = p1Id,
                            stepOrder = 1,
                            title = "1. صياغة متطلبات التطبيق",
                            nodeType = "INPUT",
                            promptTemplate = "تطبيق متابعة العادات اليومية مع إحصائيات وإشعارات وتصميم Material 3 جذاب."
                        ),
                        FlowNodeEntity(
                            projectId = p1Id,
                            stepOrder = 2,
                            title = "2. توليد الهيكل المعماري (Architecture)",
                            nodeType = "PROMPT",
                            promptTemplate = "قم بإنشاء هيكل معماري للمشروع يتضمن طبقات Data, Domain, UI مع نمط MVVM."
                        ),
                        FlowNodeEntity(
                            projectId = p1Id,
                            stepOrder = 3,
                            title = "3. بناء واجهات الـ Compose (UI Code)",
                            nodeType = "CODE_GEN",
                            promptTemplate = "اكتب كود Jetpack Compose كامل للشاشة الرئيسية مع بطاقات العادات والـ FloatingActionButton."
                        )
                    )
                )

                // Project 2: Creative AI Copywriting & Flow
                val p2Id = UUID.randomUUID().toString()
                val p2 = FlowProjectEntity(
                    id = p2Id,
                    name = "صانع المحتوى والتسويق الرقمي",
                    description = "خط أنابيب ذكي لابتكار خطط تسويقية، نصوص إعلانات، ومنشورات تفاعلية لمنصات التواصل.",
                    category = "محتوى",
                    systemPrompt = "أنت كاتب محتوى إعلاني استراتيجي ومحترف في التسويق العربي."
                )
                dao.insertProject(p2)
                dao.insertNodes(
                    listOf(
                        FlowNodeEntity(
                            projectId = p2Id,
                            stepOrder = 1,
                            title = "1. تحديد الجمهور والمنتج",
                            nodeType = "INPUT",
                            promptTemplate = "إطلاق تطبيق KALED للذكاء الاصطناعي وإنشاء المشاريع المجانية للمطورين ورواد الأعمال."
                        ),
                        FlowNodeEntity(
                            projectId = p2Id,
                            stepOrder = 2,
                            title = "2. صياغة زوايا الجذب والتسويق",
                            nodeType = "PROMPT",
                            promptTemplate = "اقترح 3 زوايا تسويقية ملهمة تركز على ميزة إنشاء المشاريع مجاناً والواجهة المستقبلية السلسة."
                        ),
                        FlowNodeEntity(
                            projectId = p2Id,
                            stepOrder = 3,
                            title = "3. كتابة المنشور النهائي وCTAs",
                            nodeType = "OUTPUT",
                            promptTemplate = "اكتب منشورين جذابين مع وسوم (Hashtags) ودعوة صريحة للتحميل والتجربة الفورية."
                        )
                    )
                )

                // Project 3: Deep Logic & Problem Solver
                val p3Id = UUID.randomUUID().toString()
                val p3 = FlowProjectEntity(
                    id = p3Id,
                    name = "محلل الأفكار والحلول الابتكارية",
                    description = "تفكيك التحديات المعقدة، التفكير الاستراتيجي، وحلول الأعمال خطوة بخطوة.",
                    category = "أعمال",
                    systemPrompt = "أنت مستشار استراتيجي وخبير حل مشكلات بأسلوب التفكير المنطقي الأول First Principles."
                )
                dao.insertProject(p3)
                dao.insertNodes(
                    listOf(
                        FlowNodeEntity(
                            projectId = p3Id,
                            stepOrder = 1,
                            title = "1. وصف التحدي أو المشكلة",
                            nodeType = "INPUT",
                            promptTemplate = "كيف يمكن تحويل فكرة تطبيق ذكاء اصطناعي إلى مشروع ناجح بدون تكاليف تشغيل باهظة؟"
                        ),
                        FlowNodeEntity(
                            projectId = p3Id,
                            stepOrder = 2,
                            title = "2. تحليل التحديات واستراتيجيات التغلب عليها",
                            nodeType = "PROMPT",
                            promptTemplate = "حلل الجوانب التقنية، التسويقية، ونموذج العمل المناسب لتحقيق استدامة ونمو سريع."
                        )
                    )
                )
            }
        }
    }
}
