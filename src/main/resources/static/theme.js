// Массив тем со значениями цветов
const themes = [
    { background: "#1a1a2e", color: "#ffffff", primaryColor: "#0f3460" }, // Тема 1: темный фон, белый текст, синий акцент
    { background: "#461220", color: "#ffffff", primaryColor: "#e94560" }, // Тема 2: бордовый фон, белый текст, розовый акцент
    { background: "#192a51", color: "#ffffff", primaryColor: "#6b4c8c" }, // Тема 3: темно-синий фон, белый текст, сиреневый акцент
    { background: "#f7b267", color: "#000000", primaryColor: "#f4845f" }, // Тема 4: светло-оранжевый фон, черный текст, красный акцент
    { background: "#f25f5c", color: "#000000", primaryColor: "#911e42" }, // Тема 5: красный фон, черный текст, бордовый акцент
];

/**
 * Функция для применения выбранной темы
 * @param {Object} theme - Объект с настройками темы (background, color, primaryColor)
 */
const setTheme = (theme) => {
    const root = document.documentElement; // Получаем корневой элемент документа
    root.style.setProperty("--background", theme.background); // Устанавливаем фон
    root.style.setProperty("--color", theme.color); // Устанавливаем основной цвет текста
    root.style.setProperty("--primary-color", theme.primaryColor); // Устанавливаем акцентный цвет

    // Сохраняем текущую тему в localStorage
    localStorage.setItem("selectedTheme", JSON.stringify(theme));
};

/**
 * Функция для отображения кнопок переключения тем
 */
const displayThemeButtons = () => {
    const container = document.querySelector(".theme-btn-container"); // Находим контейнер для кнопок тем
    themes.forEach((theme, index) => {
        const button = document.createElement("div"); // Создаем элемент для кнопки
        button.style.background = theme.background; // Устанавливаем фон кнопки в соответствии с темой
        button.className = "theme-btn"; // Добавляем класс для кнопки
        button.addEventListener("click", () => setTheme(theme)); // Добавляем обработчик клика для переключения темы
        container.appendChild(button); // Добавляем кнопку в контейнер
    });
};

/**
 * Функция для загрузки сохраненной темы из localStorage
 */
const loadSavedTheme = () => {
    const savedTheme = localStorage.getItem("selectedTheme"); // Получаем сохраненную тему
    if (savedTheme) {
        setTheme(JSON.parse(savedTheme)); // Применяем сохраненную тему
    } else {
        setTheme(themes[0]); // Применяем первую тему по умолчанию, если ничего не сохранено
    }
};

// Загрузка сохраненной темы при загрузке страницы
loadSavedTheme();

// Вызов функции для отображения кнопок
displayThemeButtons();