function attachClickHandlers() {
  document.querySelectorAll('#image-grid img').forEach(img => {
    img.addEventListener('click', () => {
      const imageDir = img.dataset.image;
      fetch('/api/selectedImage?imageDir=' + encodeURIComponent(imageDir))
        .then(res => res.json())
        .then(updateImages)
        .catch(console.error);
    });
  });
}

function updateImages(list) {
  const grid = document.getElementById('image-grid');
  grid.innerHTML = '';
  list.forEach(dir => {
    const col = document.createElement('div');
    col.className = 'col-6 col-md-3';
    col.innerHTML = `
      <div class="image-card">
        <img src="${dir}" data-image="${dir}" alt="Generated image" />
      </div>
      <div class="form-check text-center mt-1">
        <input class="form-check-input bg-dark border-0" type="checkbox" name="selectedImages" value="${dir}">
      </div>
    `;
    grid.appendChild(col);
  });
  attachClickHandlers();
}

document.addEventListener('DOMContentLoaded', () => {
  attachClickHandlers();
  document.getElementById('saveFavorites').addEventListener('click', () => {
    const data = new FormData();
    document.querySelectorAll('input[name="selectedImages"]:checked').forEach(ch => {
      data.append('selectedImages', ch.value);
    });
    fetch('/saveFavorites', { method: 'POST', body: data })
      .then(() => location.reload())
      .catch(console.error);
  });
});
