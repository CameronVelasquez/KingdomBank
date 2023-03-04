const {createApp} = Vue 

createApp({
	data(){
        return {
            objectClient: {},
            accounts: {},
            accountTransactions: undefined,
            loans: [],
            cards: [],
            cardColor: '',
            cardType: '',
            
        }

	},
	created(){
        this.loadData()
        
	},

    methods: {
        loadData (){
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
            let arrayStringNumber = cardStringNumber.split("-02-")
            return arrayStringNumber[0] + "/" + arrayStringNumber[1]
            
        },
        logOut(){
            axios.post("/api/logout")
            .then( response => {
                location.href = "/web/index.html"
            })
            .catch(error => console.log(error))
        },
        newCard(){
            axios.post("/api/clients/current/cards", `type=${this.cardType}&color=${this.cardColor}`,  {
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                }
                })
                .then(response => {
                    window.location.href = '/web/cards.html';
                })
                .catch(error => {
                    console.log(error)
                    this.error = error.response.data.message;
                });
        }
        
    }
	
}).mount('#app')