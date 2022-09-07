var id = 0;

function highlightItem(head_name) {
    let elements = document.getElementsByClassName('nav-link');
    Array.from(elements).forEach(element => {
        console.log(element.className);
        console.log(element);
        if (element.text == head_name) {
            element.classList.add("active");
        } else {
            element.classList.remove("active");
        }
    });
}