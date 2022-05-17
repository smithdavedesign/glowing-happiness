let open = false;

document.getElementsByClassName("mobile-open-close")[0].onclick = function () {
  openClose();
};

function openClose() {
  open = !open;
  const mobileContent = document.getElementsByClassName("mobile-content")[0];
  const menuIcon = document.getElementsByClassName("menu-icon")[0];
  const closeIcon = document.getElementsByClassName("menu-close-icon")[0];

  if (open) {
    mobileContent.style.display = "flex";
    menuIcon.style.display = "none";
    closeIcon.style.display = "unset";
  } else {
    mobileContent.style.display = "none";
    menuIcon.style.display = "unset";
    closeIcon.style.display = "none";
  }
}
