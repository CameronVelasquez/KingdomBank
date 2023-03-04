const {createApp} = Vue 

createApp({
	data(){
        return {
            accounts: {},
            transferenceTo: "",
            currentAccount: "",
            amount: undefined,
            originAccount: "",
            destinyAccount: "",
            description: "",
            error: "",
            errorCode: "",
            errorStatus: ""
            
        }

	},
	created(){
        this.loadData()
        
	},

    methods: {
        selectChanged(){
            this.loadData()
        },
        loadData(){
            axios.get("http://localhost:8080/api/clients/current/accounts")
                .then(response =>{
                   this.accounts = response.data    
                })
        }, 
        transfer(){
            axios.post("/api/transactions", `amount=${this.amount}&accountNumber=${this.originAccount}&destinyAccount=${this.destinyAccount}&description=${this.description}`, {
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                }
            })
            .then(response => {
                Swal.fire({
                    icon: 'success',
                    title: `${response.data}`, 
                    text: `${response.status}: CREATED`,
                    showConfirmButton: false,
                    timer: 2000,
                })
                .then(response => { window.location.href = '/web/transfers.html'; })
            })
            .catch(error => {
                console.log(error)
                Swal.fire({
                    icon: 'error',
                    title: `${error.response.data}`, 
                    text: `${error.code}: ${error.response.status}`,
                    confirmButtonColor: '#FF4B2B',
      
                })
            })
        },
        alertTransfer(){
            Swal.fire({
                title: 'Are you sure you want to Tranfer?',
                text: "You won't be able to revert this!",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#FF4B2B',
                confirmButtonText: 'Confirm'
            }).then((response) => {
                if (response.isConfirmed) {
                    this.transfer();
            }
            })
        }
        
    }
	
}).mount('#app')