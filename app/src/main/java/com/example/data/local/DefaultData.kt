package com.example.data.local

import com.example.data.model.HabitEntity
import com.example.data.model.QuoteEntity

object DefaultData {
    val defaultHabits = listOf(
        HabitEntity(
            id = 1,
            title = "Wake Up Early (05:00 AM)",
            description = "Seize the dawn before the world awakens. Victory starts with morning vigilance.",
            category = "DISCIPLINE",
            frequency = "DAILY",
            reminderTime = "05:00",
            isReminderEnabled = true
        ),
        HabitEntity(
            id = 2,
            title = "Physical Drill / Exercise",
            description = "Forge physical endurance through 45 minutes of intense bodyweight or cardio training.",
            category = "PHYSICAL",
            frequency = "DAILY",
            reminderTime = "06:30",
            isReminderEnabled = true
        ),
        HabitEntity(
            id = 3,
            title = "Hydration (3 Liters Water)",
            description = "Maintain peak physical and cognitive readiness with optimal daily water intake.",
            category = "NUTRITION",
            frequency = "DAILY",
            reminderTime = "09:00",
            isReminderEnabled = false
        ),
        HabitEntity(
            id = 4,
            title = "Read 30 Minutes",
            description = "Sharpen intellect through philosophy, strategy, or high-value non-fiction reading.",
            category = "MIND",
            frequency = "DAILY",
            reminderTime = "14:00",
            isReminderEnabled = false
        ),
        HabitEntity(
            id = 5,
            title = "Deep Study / Skill Mastery",
            description = "Unbroken, distraction-free concentration on your primary technical objective.",
            category = "STUDY",
            frequency = "WEEKDAYS",
            reminderTime = "16:00",
            isReminderEnabled = true
        ),
        HabitEntity(
            id = 6,
            title = "Mind Silence / Meditation",
            description = "15 minutes of silent observation. Calm the mind and fortify emotional control.",
            category = "MIND",
            frequency = "DAILY",
            reminderTime = "21:30",
            isReminderEnabled = true
        )
    )

    val motivationalQuotes = listOf(
        QuoteEntity(
            id = 1,
            quote = "Discipline builds destiny. What you conquer in private defines who you become in public.",
            author = "Sankalp Doctrine"
        ),
        QuoteEntity(
            id = 2,
            quote = "We do not rise to the level of our expectations, we fall to the level of our training.",
            author = "Archilochus"
        ),
        QuoteEntity(
            id = 3,
            quote = "The cost of discipline is always less than the price of regret.",
            author = "Stoic Maxim"
        ),
        QuoteEntity(
            id = 4,
            quote = "Mastery is not an accident. It is forged through unbroken daily repetitions in the dark.",
            author = "Commander Creed"
        ),
        QuoteEntity(
            id = 5,
            quote = "He who conquers himself is the mightiest warrior on any battlefield.",
            author = "Sun Tzu"
        ),
        QuoteEntity(
            id = 6,
            quote = "Motivation gets you into the arena. Unwavering discipline keeps you fighting when comfort calls.",
            author = "Sankalp Creed"
        ),
        QuoteEntity(
            id = 7,
            quote = "Small disciplines repeated with consistency every day lead to great achievements slowly gained over time.",
            author = "John C. Maxwell"
        ),
        QuoteEntity(
            id = 8,
            quote = "Iron sharpens iron. When adversity strikes, double down on your foundational habits.",
            author = "Ancient Proverb"
        ),
        QuoteEntity(
            id = 9,
            quote = "Focus is a weapon. The disciplined soldier directs it toward one objective until accomplished.",
            author = "Field Manual"
        ),
        QuoteEntity(
            id = 10,
            quote = "Never negotiate with weakness. Follow the schedule you set when you were clear-headed.",
            author = "Marcus Aurelius"
        )
    )
}
