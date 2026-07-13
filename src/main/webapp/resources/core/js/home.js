document.addEventListener("DOMContentLoaded", function () {
    const openPlayModal = document.getElementById("openPlayModal");
    const closePlayModal = document.getElementById("closePlayModal");
    const playModal = document.getElementById("playModal");

    if (openPlayModal && closePlayModal && playModal) {
        openPlayModal.addEventListener("click", function () {
            playModal.classList.add("open");
        });

        closePlayModal.addEventListener("click", function () {
            playModal.classList.remove("open");
        });

        playModal.addEventListener("click", function (event) {
            if (event.target === playModal) {
                playModal.classList.remove("open");
            }
        });

        document.addEventListener("keydown", function (event) {
            if (event.key === "Escape") {
                playModal.classList.remove("open");
            }
        });
    }
});