const {createApp} = Vue

createApp({
    data(){
        return{
        email: '',
        emailRegister: '',
        password: '',
        passwordRegister: '',
        error: '',
        errorCode: '',
        firstName: '',
        lastName: ''
    }
},
created(){
},
    methods: {
        login() {
            if(/^[a-zA-Z0-9.!#$%&'+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:.[a-zA-Z0-9-]+)$/.test(this.email)){
                axios.post('/api/login', `email=${this.email}&password=${this.password}`, {
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    }
                })
                .then(response => {
                    if (this.email == "admin@mindhub.com"){
                        window.location.href = 'manager/manager_Loans.html';
                    }
                    else{
                        window.location.href = '/web/accounts.html';
                    }
                })
                .catch(error => {
                    console.log(error)
                    Swal.fire({
                        icon: 'error',
                        title: `${error.response.data.error}`, 
                        text: `${error.code}: ${error.response.status}`,
                        confirmButtonColor: '#FF4B2B',
          
                    })
                });
            }
        },
        register() {
            if(/^[a-zA-Z0-9.!#$%&'+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:.[a-zA-Z0-9-]+)$/.test(this.emailRegister)){
                axios.post('/api/clients', `first=${this.firstName}&lastName=${this.lastName}&email=${this.emailRegister}&password=${this.passwordRegister}`, {
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    }
                })
                .then(response => {
                    axios.post('/api/login', `email=${this.emailRegister}&password=${this.passwordRegister}`, {
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded'
                        }
                        })
                        .then(response => {
                            window.location.href = '/web/accounts.html';
                        })
                        .catch(error => {
                            this.error = error.response.data.message;
                        });
                })
                .catch(error => {
                    this.error = error.response
                    this.errorCode = error.code
                    this.errorAlert()
                });
            } 
            else (console.log("Missing email"))
           
        },
        errorAlert(){
            Swal.fire({
                icon: 'error',
                title: `${this.error.data}`, 
                text: `${this.errorCode}: ${this.error.status}`,
                confirmButtonColor: '#FF4B2B',
  
            })
        }
        
    }
}).mount('#app')