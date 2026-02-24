async function loadComponent(id, file) {
    const element = document.getElementById(id);
    if (!element) return;

    const response = await fetch(file);
    const html = await response.text();
    element.innerHTML = html;

    initMobileMenu();
}

function initMobileMenu() {
    const mobileMenuBtn = document.querySelector('.mobile-menu-btn');
    const navMenu = document.querySelector('.nav-menu');

    if (mobileMenuBtn && navMenu) {
        mobileMenuBtn.addEventListener('click', () => {
            mobileMenuBtn.classList.toggle('active');
            navMenu.classList.toggle('active');
        });
    }
}

document.addEventListener("DOMContentLoaded", () => {
    loadComponent("header", "components/header.html");
    loadComponent("footer", "components/footer.html");
});