/**
 * ============================================
 * COMPONENTES REUTILIZABLES - ParkSmart
 * Funciones para crear componentes comunes del UI
 * ============================================
 */

// Asegurar que el namespace existe
window.ParkSmart = window.ParkSmart || {};

// Módulo de componentes
ParkSmart.Components = ParkSmart.Components || {};

/**
 * Crea el header de la aplicación de forma consistente
 * @param {string} currentPage - Página actual ('index', 'registro', 'salida', 'reportes', 'tarifas')
 * @returns {HTMLElement} Elemento header
 */
ParkSmart.Components.createHeader = function(currentPage) {
  const header = document.createElement('header');
  header.className = 'app-header';
  header.setAttribute('role', 'banner');
  
  // Logo
  const logoLink = document.createElement('a');
  logoLink.href = 'index.html';
  logoLink.className = 'app-header-logo';
  logoLink.setAttribute('aria-label', 'Ir a la página de inicio');
  
  const logoSvg = document.createElementNS('http://www.w3.org/2000/svg', 'svg');
  logoSvg.setAttribute('class', 'app-header-logo-icon');
  logoSvg.setAttribute('viewBox', '0 0 48 48');
  logoSvg.setAttribute('fill', 'none');
  logoSvg.setAttribute('xmlns', 'http://www.w3.org/2000/svg');
  logoSvg.setAttribute('aria-hidden', 'true');
  // ... SVG paths aquí (simplificado)
  
  const logoText = document.createElement('h1');
  logoText.className = 'app-header-logo-text';
  logoText.textContent = 'ParkSmart';
  
  logoLink.appendChild(logoSvg);
  logoLink.appendChild(logoText);
  
  // Navegación
  const nav = document.createElement('nav');
  nav.className = 'app-header-nav';
  nav.setAttribute('role', 'navigation');
  nav.setAttribute('aria-label', 'Navegación principal');
  
  const navLinks = document.createElement('ul');
  navLinks.className = 'app-header-nav-links';
  
  const pages = [
    { href: 'index.html', label: 'Inicio', id: 'index' },
    { href: 'registro.html', label: 'Entrada', id: 'registro' },
    { href: 'salida.html', label: 'Salida', id: 'salida' },
    { href: 'reportes.html', label: 'Reportes', id: 'reportes' },
    { href: 'tarifas.html', label: 'Tarifas', id: 'tarifas' }
  ];
  
  pages.forEach(function(page) {
    const li = document.createElement('li');
    const link = document.createElement('a');
    link.href = page.href;
    link.className = 'app-header-nav-link';
    link.textContent = page.label;
    link.setAttribute('aria-label', 'Ir a la página de ' + page.label.toLowerCase());
    
    if (page.id === currentPage) {
      link.setAttribute('aria-current', 'page');
    }
    
    li.appendChild(link);
    navLinks.appendChild(li);
  });
  
  nav.appendChild(navLinks);
  
  header.appendChild(logoLink);
  header.appendChild(nav);
  
  return header;
};

/**
 * Crea un modal de forma consistente
 * @param {string} id - ID del modal
 * @param {string} titulo - Título del modal
 * @param {HTMLElement} contenido - Contenido del modal
 * @param {Array} botones - Array de objetos {id, texto, tipo: 'primary'|'secondary', accion}
 * @returns {HTMLElement} Elemento modal
 */
ParkSmart.Components.createModal = function(id, titulo, contenido, botones) {
  const backdrop = document.createElement('div');
  backdrop.id = id;
  backdrop.className = 'util-hidden modal-backdrop';
  backdrop.setAttribute('role', 'dialog');
  backdrop.setAttribute('aria-labelledby', id + '-title');
  backdrop.setAttribute('aria-modal', 'true');
  
  const modal = document.createElement('div');
  modal.className = 'modal';
  
  const header = document.createElement('div');
  header.className = 'modal-header';
  const title = document.createElement('h3');
  title.id = id + '-title';
  title.className = 'modal-title';
  title.textContent = titulo;
  header.appendChild(title);
  
  const body = document.createElement('div');
  body.className = 'modal-body';
  if (typeof contenido === 'string') {
    body.innerHTML = contenido;
  } else {
    body.appendChild(contenido);
  }
  
  const footer = document.createElement('div');
  footer.className = 'modal-footer';
  
  if (botones && Array.isArray(botones)) {
    botones.forEach(function(btn) {
      const button = document.createElement('button');
      button.id = btn.id;
      button.type = 'button';
      button.className = 'btn btn-' + (btn.tipo || 'secondary');
      button.textContent = btn.texto;
      if (btn.ariaLabel) {
        button.setAttribute('aria-label', btn.ariaLabel);
      }
      if (btn.accion) {
        button.addEventListener('click', btn.accion);
      }
      footer.appendChild(button);
    });
  }
  
  modal.appendChild(header);
  modal.appendChild(body);
  modal.appendChild(footer);
  backdrop.appendChild(modal);
  
  return backdrop;
};

