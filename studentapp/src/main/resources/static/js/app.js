// ── Auto-dismiss alerts ─────────────────────────────────────
document.querySelectorAll('.alert').forEach(alert => {
  const timer = setTimeout(() => dismissAlert(alert), 4500);
  // Clear auto-timer if user manually dismisses
  alert.dataset.timer = timer;
});

function dismissAlert(alert) {
  if (!alert) return;
  alert.style.transition = 'opacity 0.4s ease, transform 0.4s ease';
  alert.style.opacity = '0';
  alert.style.transform = 'translateY(-6px)';
  setTimeout(() => alert.remove(), 420);
}

// ── Manual dismiss button ────────────────────────────────────
document.querySelectorAll('.alert-dismiss').forEach(btn => {
  btn.addEventListener('click', () => {
    const alert = btn.closest('.alert');
    if (alert.dataset.timer) clearTimeout(parseInt(alert.dataset.timer));
    dismissAlert(alert);
  });
});

// ── Delete Confirmation Modal ────────────────────────────────
function openDeleteModal(id, name, formId) {
  const modal   = document.getElementById('deleteModal');
  const nameEl  = document.getElementById('deleteTargetName');
  if (nameEl)  nameEl.textContent = name || 'this record';
  modal.dataset.formId = formId;
  modal.classList.add('open');
  document.body.style.overflow = 'hidden';
}

function closeDeleteModal() {
  const modal = document.getElementById('deleteModal');
  modal.classList.remove('open');
  document.body.style.overflow = '';
}

function confirmDelete() {
  const formId = document.getElementById('deleteModal').dataset.formId;
  const form   = formId ? document.getElementById(formId) : null;
  if (form) {
    form.submit();
  } else {
    console.error('Delete form not found:', formId);
  }
}

// Close modal on overlay click
document.getElementById('deleteModal')?.addEventListener('click', e => {
  if (e.target === e.currentTarget) closeDeleteModal();
});

// Close modal on Escape key
document.addEventListener('keydown', e => {
  if (e.key === 'Escape') closeDeleteModal();
});

// ── Active nav highlighting ──────────────────────────────────
(function() {
  const path = window.location.pathname;
  const navMap = {
    'nav-dashboard':     ['/', '/dashboard'],
    'nav-students':      ['/students'],
    'nav-courses':       ['/courses'],
    'nav-registrations': ['/registrations'],
  };

  Object.entries(navMap).forEach(([navId, prefixes]) => {
    const el = document.getElementById(navId);
    if (!el) return;
    const isActive = prefixes.some(prefix =>
      prefix === '/'
        ? (path === '/' || path === '/dashboard')
        : path.startsWith(prefix)
    );
    if (isActive) el.classList.add('active');
  });
})();

// ── Stat counter animation ───────────────────────────────────
document.querySelectorAll('.stat-value[data-target]').forEach(el => {
  const target = parseInt(el.dataset.target, 10);
  if (isNaN(target) || target === 0) return;

  const observer = new IntersectionObserver(entries => {
    entries.forEach(entry => {
      if (!entry.isIntersecting) return;
      observer.disconnect();

      let current = 0;
      const duration = 600;
      const step = Math.ceil(target / (duration / 25));
      const timer = setInterval(() => {
        current = Math.min(current + step, target);
        el.textContent = current;
        if (current >= target) clearInterval(timer);
      }, 25);
    });
  }, { threshold: 0.3 });

  observer.observe(el);
});

// ── Search — Escape to clear ─────────────────────────────────
const searchInput = document.querySelector('.search-input');
if (searchInput) {
  searchInput.addEventListener('keydown', e => {
    if (e.key === 'Escape' && searchInput.value.trim()) {
      searchInput.value = '';
      searchInput.closest('form')?.submit();
    }
  });
}
