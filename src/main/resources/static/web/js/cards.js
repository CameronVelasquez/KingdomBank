const {createApp} = Vue 

createApp({
	data(){
        return {
            objectClient: {},
            accounts: {},
            accountTransactions: undefined,
            loans: [],
            cards: [],
            transactionId: "",
            currentCardDate: undefined,          
        }

	},
	created(){
        this.loadData(),
        this.currentDate()
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
                   this.transactionId = this.accounts[0].id
                   
                   
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
        },
        /* Delete Cards */
        numberData(){
            console.log(this.number) 
        },
        
        /* Expired Cards */
        currentDate(){
            let date = new Date().toLocaleDateString()
            let day = date.split('-')[0]
            let month = date.split('-')[1]
            let year = date.split('-')[2]
            return this.currentCardDate = year + '-' + month + '-' + day
        }
        
        
    }
	
}).mount('#app')