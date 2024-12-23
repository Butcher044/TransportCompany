// Применение классов для чередования строк
function applyRowStyles() {
    const rows = document.querySelectorAll('.table-row');
    rows.forEach((row, index) => {
        row.style.backgroundColor = index % 2 === 0 ? '#FFFFFF' : '#EEEEEE';
    });
}

// Вызываем функцию при загрузке страницы
document.addEventListener('DOMContentLoaded', applyRowStyles);

// Если строки добавляются динамически, вы можете повторно вызывать applyRowStyles.
