const deleteBtns = document.getElementsByName("deleteBtn");

deleteBtns.forEach(b => {
    b.addEventListener("click", (e)  => {
        if(confirm("Are you sure you want to delete this task?")) {
            window.location.replace("/tasks")
        } else {
            e.preventDefault();
        }

    })
})


