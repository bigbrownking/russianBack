async function login(){
    let login = document.querySelector("#login")
    let password = document.querySelector("#password")
    let loginLabel = document.querySelector("#loginLabel")
    let passwordLabel = document.querySelector("#passwordLabel")
    while (loginLabel.firstChild) {
        loginLabel.removeChild(loginLabel.firstChild);
    }
    while (passwordLabel.firstChild) {
        passwordLabel.removeChild(passwordLabel.firstChild);
    }
    if(login.value.length > 4 && password.value.length > 5){
        await axios.post('/login', {params:{
            login: login.value,
            password : password.value
        }}).then(res => {
            if(res.status === 200){
                window.location.replace("/");
            }
            else if(res.data.status === 400){
                alert("Неправильный логин или пароль")
            }
        })
    }else{
        if(login.value.length <= 4){
            let error = document.createElement("div")
            error.innerText = "Длина должно быть больше 4 символа"
            error.style.color = "red"
            loginLabel.appendChild(error)
        }if(password.value.length <= 5){
            let error = document.createElement("div")
            error.innerText = "Длина должно быть больше 5 символа"
            error.style.color = "red"
            passwordLabel.appendChild(error)
        }
    }
}

async function reg(){
    let login = document.querySelector("#login")
    let password = document.querySelector("#password")
    let loginLabel = document.querySelector("#loginLabel")
    let passwordLabel = document.querySelector("#passwordLabel")
    while (loginLabel.firstChild) {
        loginLabel.removeChild(loginLabel.firstChild);
    }
    while (passwordLabel.firstChild) {
        passwordLabel.removeChild(passwordLabel.firstChild);
    }
    if(login.value.length > 4 && password.value.length > 5){
        await axios.post('/reg', {params:{
            login: login.value,
            password : password.value
        }}).then(res => {
            if(res.status === 200){
                window.location.replace("/");
            }
            else if(res.data.status === 400){
                alert("Уже существует такой логин")
            }
        })
    }else{
        if(login.value.length <= 4){
            let error = document.createElement("div")
            error.innerText = "Длина должно быть больше 4 символа"
            error.style.color = "red"
            loginLabel.appendChild(error)
        }if(password.value.length <= 5){
            let error = document.createElement("div")
            error.innerText = "Длина должно быть больше 5 символа"
            error.style.color = "red"
            passwordLabel.appendChild(error)
        }
    }
}