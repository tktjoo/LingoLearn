package com.linguaflow.app.domain.model

data class SentenceExercise(
    val targetSentence: String,
    val translation: String,
    val words: List<String>,
    val languageCode: String
)

val globalSentenceExercises = listOf(
    // EN - Inglês
    SentenceExercise("I like to eat apples", "Eu gosto de comer maçãs", listOf("I", "like", "to", "eat", "apples"), "en"),
    SentenceExercise("Where is the bathroom?", "Onde fica a casa de banho?", listOf("Where", "is", "the", "bathroom?"), "en"),
    SentenceExercise("My name is John", "O meu nome é João", listOf("My", "name", "is", "John"), "en"),
    SentenceExercise("How much does this cost?", "Quanto custa isto?", listOf("How", "much", "does", "this", "cost?"), "en"),
    SentenceExercise("I would like a coffee", "Eu gostaria de um café", listOf("I", "would", "like", "a", "coffee"), "en"),

    // ES - Espanhol
    SentenceExercise("Me gusta comer manzanas", "Eu gosto de comer maçãs", listOf("Me", "gusta", "comer", "manzanas"), "es"),
    SentenceExercise("¿Dónde está el baño?", "Onde fica a casa de banho?", listOf("¿Dónde", "está", "el", "baño?"), "es"),
    SentenceExercise("Me llamo Juan", "O meu nome é João", listOf("Me", "llamo", "Juan"), "es"),
    SentenceExercise("¿Cuánto cuesta esto?", "Quanto custa isto?", listOf("¿Cuánto", "cuesta", "esto?"), "es"),
    SentenceExercise("Quisiera un café", "Eu gostaria de um café", listOf("Quisiera", "un", "café"), "es"),

    // FR - Francês
    SentenceExercise("J'aime manger des pommes", "Eu gosto de comer maçãs", listOf("J'aime", "manger", "des", "pommes"), "fr"),
    SentenceExercise("Où sont les toilettes?", "Onde fica a casa de banho?", listOf("Où", "sont", "les", "toilettes?"), "fr"),
    SentenceExercise("Je m'appelle Jean", "O meu nome é João", listOf("Je", "m'appelle", "Jean"), "fr"),
    SentenceExercise("Combien ça coûte?", "Quanto custa isto?", listOf("Combien", "ça", "coûte?"), "fr"),
    SentenceExercise("Je voudrais un café", "Eu gostaria de um café", listOf("Je", "voudrais", "un", "café"), "fr"),

    // DE - Alemão
    SentenceExercise("Ich esse gerne Äpfel", "Eu gosto de comer maçãs", listOf("Ich", "esse", "gerne", "Äpfel"), "de"),
    SentenceExercise("Wo ist die Toilette?", "Onde fica a casa de banho?", listOf("Wo", "ist", "die", "Toilette?"), "de"),
    SentenceExercise("Mein Name ist Johannes", "O meu nome é João", listOf("Mein", "Name", "ist", "Johannes"), "de"),
    SentenceExercise("Wie viel kostet das?", "Quanto custa isto?", listOf("Wie", "viel", "kostet", "das?"), "de"),
    SentenceExercise("Ich hätte gerne einen Kaffee", "Eu gostaria de um café", listOf("Ich", "hätte", "gerne", "einen", "Kaffee"), "de"),

    // IT - Italiano
    SentenceExercise("Mi piace mangiare le mele", "Eu gosto de comer maçãs", listOf("Mi", "piace", "mangiare", "le", "mele"), "it"),
    SentenceExercise("Dov'è il bagno?", "Onde fica a casa de banho?", listOf("Dov'è", "il", "bagno?"), "it"),
    SentenceExercise("Mi chiamo Giovanni", "O meu nome é João", listOf("Mi", "chiamo", "Giovanni"), "it"),
    SentenceExercise("Quanto costa questo?", "Quanto custa isto?", listOf("Quanto", "costa", "questo?"), "it"),
    SentenceExercise("Vorrei un caffè", "Eu gostaria de um café", listOf("Vorrei", "un", "caffè"), "it"),

    // NL - Holandês
    SentenceExercise("Ik eet graag appels", "Eu gosto de comer maçãs", listOf("Ik", "eet", "graag", "appels"), "nl"),
    SentenceExercise("Waar is het toilet?", "Onde fica a casa de banho?", listOf("Waar", "is", "het", "toilet?"), "nl"),
    SentenceExercise("Mijn naam is Jan", "O meu nome é João", listOf("Mijn", "naam", "is", "Jan"), "nl"),
    SentenceExercise("Hoeveel kost dit?", "Quanto custa isto?", listOf("Hoeveel", "kost", "dit?"), "nl"),
    SentenceExercise("Ik wil graag een koffie", "Eu gostaria de um café", listOf("Ik", "wil", "graag", "een", "koffie"), "nl"),

    // KO - Coreano
    SentenceExercise("저는 사과를 먹는 것을 좋아합니다", "Eu gosto de comer maçãs", listOf("저는", "사과를", "먹는", "것을", "좋아합니다"), "ko"),
    SentenceExercise("화장실이 어디 있나요?", "Onde fica a casa de banho?", listOf("화장실이", "어디", "있나요?"), "ko"),
    SentenceExercise("제 이름은 존입니다", "O meu nome é João", listOf("제", "이름은", "존입니다"), "ko"),

    // JA - Japonês
    SentenceExercise("りんごを食べるのが好きです", "Eu gosto de comer maçãs", listOf("りんごを", "食べるのが", "好きです"), "ja"),
    SentenceExercise("トイレはどこですか？", "Onde fica a casa de banho?", listOf("トイレは", "どこですか？"), "ja"),
    SentenceExercise("私の名前はジョンです", "O meu nome é João", listOf("私の名前は", "ジョンです"), "ja"),

    // ZH - Chinês (Mandarim)
    SentenceExercise("我喜欢吃苹果", "Eu gosto de comer maçãs", listOf("我", "喜欢", "吃", "苹果"), "zh"),
    SentenceExercise("洗手间在哪里？", "Onde fica a casa de banho?", listOf("洗手间", "在", "哪里？"), "zh"),
    SentenceExercise("我叫约翰", "O meu nome é João", listOf("我", "叫", "约翰"), "zh"),

    // VI - Vietnamita
    SentenceExercise("Tôi thích ăn táo", "Eu gosto de comer maçãs", listOf("Tôi", "thích", "ăn", "táo"), "vi"),
    SentenceExercise("Phòng tắm ở đâu?", "Onde fica a casa de banho?", listOf("Phòng", "tắm", "ở", "đâu?"), "vi"),
    SentenceExercise("Tên tôi là John", "O meu nome é João", listOf("Tên", "tôi", "là", "John"), "vi"),

    // HI - Hindi
    SentenceExercise("मुझे सेब खाना पसंद ہے", "Eu gosto de comer maçãs", listOf("मुझे", "सेब", "खाना", "पसंद", "है"), "hi"),
    SentenceExercise("बाथरूम कहाँ है?", "Onde fica a casa de banho?", listOf("बाथरूम", "कहाँ", "है?"), "hi"),
    SentenceExercise("मेरा नाम जॉन है", "O meu nome é João", listOf("मेरा", "नाम", "जॉन", "है"), "hi"),

    // AR - Árabe
    SentenceExercise("أحب أكل التفاح", "Eu gosto de comer maçãs", listOf("أحب", "أكل", "التفاح"), "ar"),
    SentenceExercise("أين الحمام؟", "Onde fica a casa de banho?", listOf("أين", "الحمام؟"), "ar"),
    SentenceExercise("اسمي جون", "O meu nome é João", listOf("اسمي", "جون"), "ar"),

    // TH - Tailandês
    SentenceExercise("ฉันชอบกินแอปเปิ้ล", "Eu gosto de comer maçãs", listOf("ฉัน", "ชอบ", "กิน", "แอปเปิ้ล"), "th"),
    SentenceExercise("ห้องน้ำอยู่ที่ไหน?", "Onde fica a casa de banho?", listOf("ห้องน้ำ", "อยู่", "ที่ไหน?"), "th"),
    SentenceExercise("ฉันชื่อจอห์น", "O meu nome é João", listOf("ฉัน", "ชื่อ", "จอห์น"), "th")
)
