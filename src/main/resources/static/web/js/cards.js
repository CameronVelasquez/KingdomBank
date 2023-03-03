const {createApp} = Vue 

createApp({
	data(){
        return {
            objectClient: {},
            accounts: {},
            accountTransactions: undefined,
            loans: [],
            cards: [],
            
        }

	},
	created(){
        this.cargarDatos()
        
	},

    methods: {
        cargarDatos (){
            axios.get("http://localhost:8080/api/clients/current")
                .then(response =>{
                   this.objectClient =response.data
                   this.accounts = this.objectClient.accounts
                   this.accountTransactions = this.accounts.forEach(element => console.log(element.transactions.slice(0, 3)));
                   this.loans = this.objectClient.loans
                   this.cards = this.objectClient.cards
                   
                   
                   
                })
        },
        cardDate(cardStringNumber){ 
            let arrayStringNumber = cardStringNumber.split("-")
            return arrayStringNumber[0] + "/" + arrayStringNumber[1].slice(0)
            
        },
        logOut(){
            axios.post("/api/logout")
            .then( response => {
                location.href = "/web/index.html"
            })
            .catch(error => console.log(error))
        }
        
    }
	
}).mount('#app')