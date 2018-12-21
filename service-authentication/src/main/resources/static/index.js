function submit() {
    let password = document.getElementById("password").value;
}


function httpRequest(url, param, callback, ref) {
    fetch(url, param).then(function (response) {
        if (response.ok) {
            return response.json();
        } else
            throw new Error(response.statusText);
    }).then(function (data) {
        // console.log(data);
        if (data.code === '000000') {
            if (callback !== undefined)
                callback(data, ref);
        } else
            alert(data.msg);
    }).catch(function (err) {
        console.log(err);
        alert(err);
    });
}


function login() {
    var userName = document.getElementById("userName").value;
    var passWord = document.getElementById("passWord").value;


    var param = {
        method: 'GET',
        mode: 'cors'
    };

    httpRequest('http://localhost:8080/security/rsaPublicKey', param, function (data) {
        console.log(data);
        data = data.data;
        var encrypt = new JSEncrypt();
        encrypt.setPublicKey(data.publicKey);
        passWord = encrypt.encrypt(passWord);
        param.headers = {
            "passWord": passWord,
            "keyId": data.keyId
        };
        console.log(passWord);

        httpRequest("http://localhost:8080/users/" + userName, param, function (data) {
            var storage = window.localStorage;
            storage['token'] = data.token;
        });

    });
}

