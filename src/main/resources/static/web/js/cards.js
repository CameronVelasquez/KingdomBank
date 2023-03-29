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
            number: "", 
            showCard: false         
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
                   console.log(this.cards)
                   
                   
                })
        },
        cardDate(cardStringNumber){ 
            let arrayStringNumber = cardStringNumber.split("-")
            return arrayStringNumber[0] + "/" + arrayStringNumber[1].slice(0)
            
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
        /* Delete Cards */
        deleteCards(){
            console.log(this.number) 
            axios.patch("/api/clients/current/cards", `number=${this.number}`, {
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
                    window.location.href = '/web/cards.html'; this.loadData()
                })
            })
            
        },
        alertDeleteCard(){
            Swal.fire({
                title: 'Are you sure you want to delete this card?',
                text: "You won't be able to revert this!",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#FF4B2B',
                confirmButtonText: 'Confirm'
            }).then((response) => {
                if (response.isConfirmed) {
                    this.deleteCards();
            }
            })
        },

        
        /* Expired Cards */
        currentDate(){
            let date = new Date().toLocaleDateString()
            let day = date.split('-')[0]
            let month = date.split('-')[1]
            let year = date.split('-')[2]
            return this.currentCardDate = year + '-' + month + '-' + day
        },
        toggleCard() {
            this.showCard = !this.showCard;
        }
        
        
    },
	
}).mount('#app')