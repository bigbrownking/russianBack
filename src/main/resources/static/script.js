let path =  window.location.href
let thisPath = path.split("/")
let userInfo = {
    id : null,
    login : null,
    isAdmin: null,
    favorites: null
}
let query = {
    category: null,
    search: null
}
async function isLogin(){
    await axios.get("isLogin").then(res=>{
        if(res.status === 200){
            userInfo = {
                id : res.data["_id"],
                login : res.data.username,
                isAdmin: res.data.admin,
                favorites: res.data.favorites
            }
        }
        else{
            userInfo = {
                id : null,
                login : null,
                isAdmin: null,
                favorites: null
            }
        }

    }).catch(error => {
        console.error('Ошибка:', error);
    });
    if(userInfo.id !== null && userInfo.login !== null){
        let AdminPageBody = document.querySelector(".AdminPageBody")
        console.log(AdminPageBody)
        if(userInfo.isAdmin){
            let adminPage = document.querySelector("#adminPageNav")
            adminPage.style.display = "inline"
            if(AdminPageBody){
                AdminPageBody.style.display = "inline"
            }
        }else{
            if(AdminPageBody){
                AdminPageBody.style.display = "none"
            }
        }
        let loginPage = document.querySelector("#loginPage")
        loginPage.style.display = "none"
        let logoutPage = document.querySelector("#logoutPage")
        logoutPage.style.display = "inline"
        let profile = document.querySelector("#profilePage")
        profile.style.display = "inline"

        let currentPage = thisPath[thisPath.length - 1].slice(0, -1)
        if(currentPage === "profile"){
            let ProfilePageBody = document.querySelector(".ProfilePageBody")
            if(ProfilePageBody){
                ProfilePageBody.style.display = "inline"
            }
        }
    }
    else{
        let AdminPageBody = document.querySelector(".AdminPageBody")
        if(AdminPageBody){
            AdminPageBody.style.display = "none"
        }
        let ProfilePageBody = document.querySelector(".ProfilePageBody")
        if(ProfilePageBody){
            ProfilePageBody.style.display = "none"
        }
    }
    if(thisPath[thisPath.length - 1].length > 2){
        let currentPage = thisPath[thisPath.length - 1].slice(0, -1)
        let nav = document.querySelector("#" + currentPage)
        nav.className = "nav-link active"
        if(currentPage === "profile"){
            let table = document.querySelector(".profileInformation")
            table.innerHTML = `
        <tr>
            <th scope="row">ID</th>
            <td>${userInfo.id}</td>
        </tr>
        <tr>
            <th scope="row">Логин</th>
            <td>${userInfo.login}</td>
        </tr>`
        }
    }else{
        let nav = document.querySelector("#main")
        nav.className = "nav-link active"
    }

}
isLogin()


async function getCategories(){
    let category = document.querySelector("#changeCategory")
    while (category.firstChild) {
        category.removeChild(category.firstChild);
    }
    let option = document.createElement("option")
    option.innerText = "Все"
    option.value = "all"
    option.selected
    category.appendChild(option)
    await axios.get("/getCategories").then(res=>{
        res.data.forEach(cat =>{
            let option = document.createElement("option")
            option.innerText = cat
            option.value = cat
            option.id = cat
            category.appendChild(option)
        })
    }).catch(error => {
        console.error(error)
    })
}
getCategories()
getProverbs()

async function changeCategory(){
    let category = document.querySelector("#changeCategory")
    let searchWord =  document.querySelector("#searchWord")
    if(searchWord && searchWord.value && searchWord.value.length > 0){
        query.search = searchWord.value
    }else{
        if(query.search){
            query.search = null
        }
    }
    console.log(category.value); 
    if(category && category.value && category.value === "all"){
        query.category = null
    }else{
        query.category = category.value
    }
    await getProverbs()
}

async function searchByWord(){
    let searchWord =  document.querySelector("#searchWord")

    let category = document.querySelector("#changeCategory")
    console.log(category.value);
    if(category && category.value && category.value === "all"){
        query.category = null
    }else{
        query.category = category.value
    }
    if(searchWord && searchWord.value && searchWord.value.trim().length > 0){
        query.search = searchWord.value.toLowerCase()
    }else{
        if(query.search){
            query.search = null
        }
        alert("Введите слово поиска")
    }
    await getProverbs()
}
async function clearInput() {
    let clearSearchInput = document.querySelector("#clearSearchInput")
    let searchWord =  document.querySelector("#searchWord")
    searchWord.value = ""
    let category = document.querySelector("#changeCategory")
    console.log(category.value);
    if(category && category.value && category.value === "all"){
        query.category = null
    }else{
        query.category = category.value
    }
    query.search = null
    await getProverbs()
}

async function getProverbs(){
    let data
    try {
        console.log(query)
        await axios.post("/getProverbs", {query}).then(response => {

            data = response.data
            console.log(response.data);
        }).catch(error => {
                console.error('Ошибка:', error);    
        });
        let clearSearchInput = document.querySelector("#clearSearchInput")
        if(query.search !== null){
            clearSearchInput.style.display = "inline"
        }else{
            clearSearchInput.style.display = "none"
        }
        showProverbs(data, false)
    }
    catch (error) {
        console.error("Произошла ошибка при отправке запроса", error);
    }
}

async function logout(){
    await axios.get("/logOut", {query : {id : userInfo.id}})
    userInfo = {
        id : null,
        login : null,
        isAdmin: false,
        favorites: null
    }
    let adminPage = document.querySelector("#adminPageNav")
    adminPage.style.display = "none"
    let loginPage = document.querySelector("#loginPage")
    loginPage.style.display = "inline"
    let logoutPage = document.querySelector("#logoutPage")
    logoutPage.style.display = "none"
    let profile = document.querySelector("#profilePage")
    profile.style.display = "none"
    window.location.replace("/");
}

async function deleteProverb(proverbId) {
    await axios.post("/deleteProverb", {params: {proverbId: proverbId, isProfile: false, id: userInfo.id}})
    if(userInfo.favorites.some(value => { return value["_id"] === proverbId })){
        deleteProverbFromProfile(proverbId)
    }
    location.reload()
}
async function deleteProverbFromProfile(proverbId) {
    await axios.post("/deleteProverb", {params: {proverbId: proverbId, isProfile: true, id: userInfo.id}})
    location.reload()
}

async function addToFav(proverbId) {
    if(userInfo.id){
        await axios.post("/addToFav", {params: {proverbId: proverbId, userId: userInfo.id}})
        location.reload()
    }
    else{
        window.location.replace("/login");
    }

}

function showProverbs(data, isProfile) {
    if (data) {
        let table = document.querySelector(".tableProverbs")
        while (table.firstChild) {
            table.removeChild(table.firstChild);
        }
        for (let prov of data) {
                let div =document.createElement("div")
                let starIcon = null
                if((userInfo.favorites && userInfo.favorites.some(value => { return value["_id"] === prov["_id"] })) || isProfile){
                    starIcon = "<svg style=\"width: 25px; height: 25px;\" xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 576 512\"><path fill=\"#FFD43B\" d=\"M316.9 18C311.6 7 300.4 0 288.1 0s-23.4 7-28.8 18L195 150.3 51.4 171.5c-12 1.8-22 10.2-25.7 21.7s-.7 24.2 7.9 32.7L137.8 329 113.2 474.7c-2 12 3 24.2 12.9 31.3s23 8 33.8 2.3l128.3-68.5 128.3 68.5c10.8 5.7 23.9 4.9 33.8-2.3s14.9-19.3 12.9-31.3L438.5 329 542.7 225.9c8.6-8.5 11.7-21.2 7.9-32.7s-13.7-19.9-25.7-21.7L381.2 150.3 316.9 18z\"/></svg>\n"
                }else{
                    starIcon = "<svg style=\"width: 25px; height: 25px;\" xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 576 512\"><path d=\"M287.9 0c9.2 0 17.6 5.2 21.6 13.5l68.6 141.3 153.2 22.6c9 1.3 16.5 7.6 19.3 16.3s.5 18.1-5.9 24.5L433.6 328.4l26.2 155.6c1.5 9-2.2 18.1-9.7 23.5s-17.3 6-25.3 1.7l-137-73.2L151 509.1c-8.1 4.3-17.9 3.7-25.3-1.7s-11.2-14.5-9.7-23.5l26.2-155.6L31.1 218.2c-6.5-6.4-8.7-15.9-5.9-24.5s10.3-14.9 19.3-16.3l153.2-22.6L266.3 13.5C270.4 5.2 278.7 0 287.9 0zm0 79L235.4 187.2c-3.5 7.1-10.2 12.1-18.1 13.3L99 217.9 184.9 303c5.5 5.5 8.1 13.3 6.8 21L171.4 443.7l105.2-56.2c7.1-3.8 15.6-3.8 22.6 0l105.2 56.2L384.2 324.1c-1.3-7.7 1.2-15.5 6.8-21l85.9-85.1L358.6 200.5c-7.8-1.2-14.6-6.1-18.1-13.3L287.9 79z\"/></svg>\n"
                }
                div.innerHTML = `
                <div class="card mb-3" id="${prov['_id']}">
                    <div class="card-header">
                        ${prov.category}
                    </div>
                    <div class="d-flex">
                        <div class="px-2 pb-3" style="display: flex; flex-direction: column; justify-content: center;">
                            <button id="fav${prov['_id']}" class="btn p-0">
                                ${starIcon}
                            </button>
                        </div>
                        <div class="card-body" style="padding-left: 0px;">
                            <h5 class="card-title">${prov.description}</h5>
                            <p class="card-text">${prov.meaning}</p>
                        </div>
                        <div class="px-2 pb-3" style="display: ${userInfo.isAdmin || isProfile ? "flex" : "none" }; flex-direction: column; justify-content: center;">
                            <button id="del${prov['_id']}" class="btn btn-danger">Удалить</button>
                        </div>
                    </div>            
                </div>
                `
                table.appendChild(div)
                let favAdd = document.querySelector(`#fav${prov["_id"]}`)
                favAdd.addEventListener("click", ()=>{
                    addToFav(prov["_id"])
                })

                let delProv = document.querySelector(`#del${prov["_id"]}`)
                if(isProfile){
                    delProv.addEventListener("click", ()=>{
                        deleteProverbFromProfile(prov["_id"])
                    })
                }else{
                    delProv.addEventListener("click", ()=>{
                        deleteProverb(prov["_id"])
                    })
                }

            }
    }
}

async function getData() {
    let data
    try {
        await axios.post("/getFavProverbs", {params: {id: userInfo.id}}).then(response => {

            data = response.data
            console.log(response.data);
        })
            .catch(error => {
                console.error('Ошибка:', error);    
            });
        showProverbs(data, true)
    }
    catch (error) {
        console.error("Произошла ошибка при отправке запроса", error);
    }
}