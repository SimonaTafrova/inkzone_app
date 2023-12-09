





const tableBody = document.getElementById('table-body');


const csrfHeaderName = document.head.querySelector('[name="_csrf_header"]').content;
const csrfHeaderValue = document.head.querySelector('[name="_csrf"]').content;
const itemForm = document.getElementById('itemForm');
const editForm = document.getElementById('editItemForm');

itemForm.addEventListener("submit", handleItemSubmit);
editForm.addEventListener("submit", handleEditItemSubmit);
let categoryName = document.getElementById('itemCategory').textContent.toLocaleLowerCase();
const itemArray = [];







async function handleItemSubmit(event) {
    event.preventDefault();
    const form = event.currentTarget;
    const url = form.action;
    const formData = new FormData(form);
    const method = "POST";

    try {
        const responseData = await postFormDataAsJson({url, formData, method});
        if(responseData !== undefined) {
            let formValues = (Object.fromEntries(formData));
            if (formValues.category.toLowerCase() === categoryName.slice(0, -1)) {
                insertNewRow(responseData);
            }
            form.reset();
        }
    } catch (error) {

       console.log(error)

    }




}

async function handleEditItemSubmit(event){
    event.preventDefault();
    console.log("Edit")
    const form = event.currentTarget;
    const url = form.action;
    const formData = new FormData(form);
    const method = "PUT";

    try {
        const response = await postFormDataAsJson({url, formData, method});
        if(response !== undefined) {
            form.reset();
            window.location.replace(`/stock/${categoryName}`)
        }
    } catch (error) {
        console.log(error)






    }
}

async function postFormDataAsJson({url, formData, method}) {

    const plainFormData = Object.fromEntries(formData.entries());
    let isValid = true;

    if(plainFormData.name === "" || plainFormData.quantity === "" || plainFormData.quantity <= -1
        || plainFormData.minQuantity === "" || plainFormData.minQuantity <= -1 || plainFormData.category === ""){

        alert("All fields are mandatory and quantities can not be negative numbers!")
        isValid = false;


    }

    let currentId = url.split("/")[6];

    for(let item of itemArray){
        if(item.name === plainFormData.name && item.id.toString() !== currentId){
            alert(item.name + " already exists!")
            isValid = false;
        }
    }
    if(isValid) {
        let newItem = {
            name: plainFormData.name,
            quantity: plainFormData.quantity,
            minQuantity: plainFormData.minQuantity,
            category: plainFormData.category
        }
        const formDataAsJSONString = JSON.stringify(newItem);
        console.log(formDataAsJSONString);

        const fetchOptions = {
            method: method,
            headers: {
                [csrfHeaderName]: csrfHeaderValue,
                "Content-Type": "application/json",
                "Accept": "application/json"
            },
            body: formDataAsJSONString
        }

        console.log(fetchOptions)

        const response = await fetch(url, fetchOptions);

        if (!response.ok) {
            const errorMessage = await response.text();
            throw new Error(errorMessage);
        }

        return response.json();
    }
}




    function insertNewRow(item){
        let newRow = document.createElement('tr');

        let tdName = document.createElement('td');
        tdName.textContent = item.name;
        let tdQuantity = document.createElement('td');
        tdQuantity.textContent = item.quantity;
        let tdMinQuantity = document.createElement('td');
        tdMinQuantity.textContent = item.minQuantity;
        let buttons = document.createElement('td');
        let editBtn = document.createElement('button');
        editBtn.classList.add('btn');
        editBtn.classList.add('btn-outline-light');
        editBtn.textContent = "Edit"
        editBtn.addEventListener('click', () => {
            let editModal = document.getElementById("editItemModal");
            editModal.setAttribute('aria-hidden', false)
            editModal.setAttribute('aria-modal', true)
            editModal.classList.add('show');
            editModal.style.display = 'block';
            let editForm = document.getElementById('editItemForm');
            editForm.action = `/api/stock/update/${item.id}`;
            let editName = document.getElementById('editName')
            let editQuantityValue = document.getElementById('editQuantity');
            let editMinQuantityValue = document.getElementById('editMinQuantity');
            let editCategoryValue = document.getElementById('editCategory');
            editName.value = item.name;
            editQuantityValue.value = item.quantity;
            editMinQuantityValue.value = item.minQuantity;



            let closeBtn = document.getElementById('closeEditModal');
            closeBtn.addEventListener('click', () => {
                editModal.setAttribute('aria-hidden', true)
                editModal.setAttribute('aria-modal', false)
                editModal.classList.remove('show');
                editModal.style.display = 'none';

            })

        })
        let deleteBtn = document.createElement("button");
        deleteBtn.classList.add('btn');
        deleteBtn.classList.add("btn-outline-light");
        deleteBtn.type = "submit";
        deleteBtn.textContent = 'Delete';
        let deleteForm = document.createElement("form");
        deleteForm.method = "delete";
        deleteForm.action = `/api/stock/${item.id}`
        deleteForm.appendChild(deleteBtn);
        buttons.appendChild(editBtn);
        buttons.appendChild(deleteForm);
        newRow.appendChild(tdName);
        newRow.appendChild(tdQuantity);
        newRow.appendChild(tdMinQuantity);
        newRow.appendChild(buttons);

        tableBody.appendChild(newRow);

        console.log(csrfHeaderValue + csrfHeaderValue)
        deleteBtn.addEventListener('click', ()  => {
            if(confirm("Are you sure you want to delete this item?")) {
                tableBody.removeChild(newRow)
                fetch(`http://localhost:8080/api/stock/${item.id}`, {
                        method: "DELETE",
                        headers: {
                            [csrfHeaderName]: csrfHeaderValue,
                            "Content-Type": "application/json",
                            "Accept": "application/json"
                        }
                    }
                ).then(() => {
                    console.log("removed");
                })
                    .catch(error => {
                        console.error(error)
                    })


            }


        })




    }





{
    fetch(`http://localhost:8080/api/stock/${categoryName}`,{
        headers: {
            "Accept": "application/json"
        }
    }).then(res => res.json())
        .then(data => {
            for(let item of data){
                insertNewRow(item);
                itemArray.push(item);
            }

        })


}


function sortTable(){
    var table, rows, switching, i, x, y, shouldSwitch;
    table = document.getElementById("itemTable");
    switching = true;
    /* Make a loop that will continue until
    no switching has been done: */
    while (switching) {
        // Start by saying: no switching is done:
        switching = false;
        rows = table.rows;
        /* Loop through all table rows (except the
        first, which contains table headers): */
        for (i = 1; i < (rows.length - 1); i++) {
            // Start by saying there should be no switching:
            shouldSwitch = false;
            /* Get the two elements you want to compare,
            one from current row and one from the next: */
            x = rows[i].getElementsByTagName("TD")[0];
            y = rows[i + 1].getElementsByTagName("TD")[0];
            // Check if the two rows should switch place:
            if (x.innerHTML.toLowerCase() > y.innerHTML.toLowerCase()) {
                // If so, mark as a switch and break the loop:
                shouldSwitch = true;
                break;
            }
        }
        if (shouldSwitch) {
            /* If a switch has been marked, make the switch
            and mark that a switch has been done: */
            rows[i].parentNode.insertBefore(rows[i + 1], rows[i]);
            switching = true;
        }
    }
}











