const {createApp} = Vue 

createApp({
	data(){
        return {
            objectClient: {},
            accounts: {},
            accountTransactions: undefined,
            loans: [],
            type: '',
            color: '',
            transactionId: undefined,
            accountNumber: undefined,
            destinyAccount: undefined,
            accountType: "",
        }

	},
	created(){
        this.loadData()
        
	},

    methods: {
        loadData (){
            axios.get("http://localhost:8080/api/clients/current")
                .then(response =>{
                   this.objectClient = response.data
                   this.accounts = this.objectClient.accounts
                   this.accountTransactions = this.accounts.forEach(element => console.log(element.transactions.slice(0, 3)));
                   this.loans = this.objectClient.loans
                   this.transactionId = this.accounts[0].id
                   
                   
                   
                })
                .catch(error => console.log(error))
        },
        logOut(){
            axios.post("/api/logout")
            .then( response => {
                Swal.fire({
                    icon: 'success',
                    title: `Successfully Log out!`, 
                    text: `${response.status}: OK`,
                    showConfirmButton: false,
                    timer: 2000,
                })
                .then(response => {
                    location.href = "/web/index.html"
                })
            })
            .catch(error => console.log(error))
        },
        createAccounts(){
            axios.post("/api/clients/current/accounts", `accountType=${this.accountType}`)
            .then( response => {   
                Swal.fire({
                    icon: 'success',
                    title: `Congratulations!`, 
                    text: `You've created a new account successfully.`,
                    confirmButtonColor: '#FF4B2B',
      
                })
                .then( response => {
                    this.loadData()
                    location.href = "/web/accounts.html"
                })
                
            })
        },
        deleteAccounts(){
            axios.patch("/api/clients/current/accounts", `accountNumber=${this.accountNumber}&accountDestiny=${this.destinyAccount}`, {
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                }
                })
            .then(response => {
                Swal.fire({
                    icon: 'success',
                    title: `${response.data}`, 
                    text: `${response.status}: OK`,
                    showConfirmButton: false,
                    timer: 2000,
                })
                .then(response => {
                    window.location.href = '/web/accounts.html'; this.loadData()
                })
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
        alertDeleteAccount(){
            Swal.fire({
                title: 'Are you sure you want to delete this account?',
                text: "You won't be able to revert this!",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#FF4B2B',
                confirmButtonText: 'Confirm'
            }).then((response) => {
                if (response.isConfirmed) {
                    this.deleteAccounts();
            }
            })
        }
        
    }
	
}).mount('#app')