let onCloseModal = null
let modal = null
function show(){
    modal =  new bootstrap.Modal(document.querySelector("#exampleModalCenter"))
    modal.toggle();
    if(modal !== null){
        onCloseModal = document.querySelector("#exampleModalCenter")
        onCloseModal.addEventListener('hidden.bs.modal', cancleAddProverb);
    }
}

function showUsers(data) {
    if (data) {
        let table = document.querySelector(".tableUsers")
        while (table.firstChild) {
            table.removeChild(table.firstChild);
        }
        table.style.border = "1px solid #000"
        var tr = document.createElement('tr');
        Object.keys(data[0]).map(key => {
            var td = document.createElement('td');
            td.innerText = key
            td.style.border = "1px solid #000"
            tr.appendChild(td)
        })
        var tbdy = document.createElement('tbody');
        tbdy.appendChild(tr);
        for (let user of data) {
            var tr = document.createElement('tr');
            Object.keys(user).map((key, id) => {
                var td = document.createElement('td');
                console.log(key)
                if(key === "favorites"){
                    let inner = ""
                    user[key].forEach(prov =>{
                        inner += `>>${prov.description}\n `
                    })
                    td.innerText = inner
                    td.style.border = "1px solid #000"
                    td.style.padding = "3px"
                    tr.appendChild(td)
                }else{
                    td.innerText = user[key]
                    td.style.border = "1px solid #000"
                    td.style.padding = "3px"
                    tr.appendChild(td)
                }
            })
            //_________________delete user______________________
            var td = document.createElement('button');
            td.innerText = "Delete"
            td.id = user["_id"]
            console.log(user["_id"])
            td.addEventListener("click", () => {
                deleteUser(user["_id"])
            })
            td.style.border = "1px solid #000"
            td.style.padding = "3px"
            tr.appendChild(td)

            tbdy.appendChild(tr);
        }
        table.appendChild(tbdy);
    }
}

async function getData() {
    let data
    try {
        await axios.get("/getUsers").then(response => {

            data = response.data
            console.log(response.data);
        })
            .catch(error => {
                console.error('Ошибка:', error);    
            });
        showUsers(data)
    }
    catch (error) {
        console.error("Произошла ошибка при отправке запроса", error);
    }
}
async function deleteUser(id) {
    let data = null;
    try {
        console.log(id);
        await axios.post("/deleteUser", { id: id }).then(response => {
            data = response.data
            console.log(response.data);
        })
            .catch(error => {
                console.error('Ошибка:', error);
            });
        if (data.status === 200) {
            alert(data.message + " " + id)
            getData()
        }
    }
    catch (error) {
        console.error("Произошла ошибка при отправке запроса", error);
    }
}
async function addProverb() {
    try {
        let title = document.getElementById("titleOfNew").value
        let discription = document.getElementById("discriptionOfNew").value
        let category = document.getElementById("categoryOfNew").value
        if (category === null || category.trim() === ""){
            alert("Введите категрию")
            return;
        }
        if (title == null || title.trim() == ""){
            alert("Введите пословицу")
            return;
        }
        if (discription === null || discription.trim() === ""){
            alert("Введите значение полсовицы")
            return;
        }
        modal.toggle()  
        await axios.post("/addProverb", {params: { "meaning": title, "description": discription, "category": category}})
            .then(response => {
                console.log(response.data);
                if(response.data.id){
                    alert("Успешно. ID пословицы: "+response.data.id)
                }
            })
            .catch(error => {
                console.error('Ошибка:', error);
            });
    } catch (error) {
        console.error("Произошла ошибка при отправке запроса", error);
    }
}

function cancleAddProverb() {
    userIdForUpdate = null
    onCloseModal = null
    modal = null
    console.log(modal);
}