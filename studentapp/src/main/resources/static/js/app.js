// ── Auto-dismiss alerts ────────────────────────────────────
document.querySelectorAll('.alert').forEach(alert => {
  setTimeout(() => {
    alert.style.transition = 'opacity 0.5s, transform 0.5s';
    alert.style.opacity = '0';
    alert.style.transform = 'translateY(-8px)';
    setTimeout(() => alert.remove(), 500);
  }, 4000);
});

// ── Dismiss button ──────────────────────────────────────────
document.querySelectorAll('.alert-dismiss').forEach(btn => {
  btn.addEventListener('click', () => {
    const alert = btn.closest('.alert');
    alert.style.transition = 'opacity 0.3s';
    alert.style.opacity = '0';
    setTimeout(() => alert.remove(), 300);
  });
});

// ── Delete confirmation modals ──────────────────────────────
function openDeleteModal(id, name, formId) {
  const modal = document.getElementById('deleteModal');
  const nameEl = document.getElementById('deleteTargetName');
  const form   = document.getElementById(formId || 'deleteForm');
  if (nameEl) nameEl.textContent = name || 'this record';
  modal.classList.add('open');
  modal.dataset.formId = formId;
}

function closeDeleteModal() {
  document.getElementById('deleteModal').classList.remove('open');
}

function confirmDelete() {
  const formId = document.getElementById('deleteModal').dataset.formId;
  if (formId) {
    document.getElementById(formId).submit();
  }
}

// Close modal on overlay click
document.addEventListener('click', e => {
  if (e.target.id === 'deleteModal') closeDeleteModal();
});

// ── Active nav highlighting ─────────────────────────────────
const path = window.location.pathname;
document.querySelectorAll('.nav-item').forEach(link => {
  const href = link.getAttribute('href');
  if (href && href !== '/' && path.startsWith(href)) {
    link.classList.add('active');
  } else if (href === '/' && (path === '/' || path === '/dashboard')) {
    link.classList.add('active');
  }
});

// ── Animate stat counters ───────────────────────────────────
function animateCounter(el) {
  const target = parseInt(el.dataset.target, 10);
  if (isNaN(target)) return;
  let current = 0;
  const step = Math.ceil(target / 40);
  const timer = setInterval(() => {
    current = Math.min(current + step, target);
    el.textContent = current;
    if (current >= target) clearInterval(timer);
  }, 25);
}

document.querySelectorAll('.stat-value[data-target]').forEach(el => {
  const observer = new IntersectionObserver(entries => {
    entries.forEach(entry => {
      if (entry.isIntersecting) {
        animateCounter(el);
        observer.disconnect();
      }
    });
  });
  observer.observe(el);
});

// ── Search — clear button ───────────────────────────────────
const searchInput = document.querySelector('.search-input');
if (searchInput && searchInput.value) {
  searchInput.addEventListener('keydown', e => {
    if (e.key === 'Escape') {
      searchInput.value = '';
      searchInput.closest('form').submit();
    }
  });
}
