
let deleteBtn = document.querySelectorAll('#deleteBtn');




deleteBtn.forEach(button => {
    button.addEventListener("click", (e) => {

            if (!confirm("Are you sure you want to delete this user?")) {

                e.preventDefault();
                window.location.replace(`/admin`);
            }


        })

    })
