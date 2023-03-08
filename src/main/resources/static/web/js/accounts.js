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
            transactionId: undefined
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
            axios.post("/api/clients/current/accounts")
            .then( response => {   
                Swal.fire({
                    icon: 'success',
                    title: `Congratulations!`, 
                    text: `You've created a new account successfully.`,
                    confirmButtonColor: '#FF4B2B',
      
                })
                .then( response => {
                    this.loadData()
                })
                
            })
        }
        
    }
	
}).mount('#app')