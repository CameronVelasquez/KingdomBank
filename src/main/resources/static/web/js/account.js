const {createApp} = Vue 

createApp({
	data(){
        return {
            account: [],
            dataAccounts: {},
            objectAccount:  [],
            objectTransactions: [],
            objectClient: {},
            /* fromAccount: null,
            startDate: "",
            endDate: null,
            description: "",
            maxAmount: null,
            minAmount: null,
            type: null, */
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
            transactions: [],

            
            
        }

	},
	created(){
        this.loadDataCuenta()
        this.loadDataClx()
        this.loadData()
	},
    methods: {
        loadData (){
            axios.get("http://localhost:8080/api/clients/current")
                .then(response =>{
                   this.objectClient = response.data
                   this.accounts = this.objectClient.accounts
                   this.loans = this.objectClient.loans
                   this.transactionId = this.accounts[0].id
                   
                   
        
                })
                .catch(error => console.log(error))
        },
        loadDataCuenta(){
            let id = new URLSearchParams(location.search).get("id")
            axios("http://localhost:8080/api/clients/current/accounts")
            .then(response =>{
                this.dataAccounts = response.data
                this.objectAccount = this.dataAccounts.find(account => account.id)
                this.accountNumber = this.objectAccount.number
                this.transactions = this.objectAccount.transactions.sort((a, b) => new Date(b.date).getTime() - new Date(a.date).getTime())
                console.log(this.transactions +"hola")
               
            
                /* this.loadTransactions() */
            })
            .catch(error => console.log(error))
        },
     /*    loadTransactions() {
            axios.get("/api/clients/current/transactions", {
                params: {
                    fromAccount: this.accountNumber,
                    startDate: this.startDate,
                    endDate: this.endDate,
                    description: this.description,
                    maxAmount: this.maxAmount,
                    minAmount: this.minAmount,
                    type: this.type
                }
            }).then(response => {
                this.objectTransactions = response.data.sort((a, b) => new Date(b.date).getTime() - new Date(a.date).getTime())
                
            })
                .catch(err => console.log(err));
        },
        printPdf(){
            const element=document.querySelector(".print-content");
            let opt = {
                margin: 0.5,
                filename: 'TransactionsSummary.pdf',
                image: { type: 'jpeg', quality: 0.98 },
                html2canvas: { scale: 1 },
                jsPDF: { unit: 'in', format: 'letter', orientation: 'portrait' }
            };
            html2pdf().set(opt).from(element).save();
        }, */
        loadDataClx(){
            axios.get("http://localhost:8080/api/clients/current")
            .then(response =>{
                this.objectClient = response.data
            })
            .catch(error => console.log(error))
        },
        justDay (data){
            return data.split("T")[0]
        },
        hour (data){
            let justHour = data.split("T")[1]
            return justHour.substr(0, 8)
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
            }       
       
  }/*  */
  /* computed:{
    elementsFiltered() {
        return this.transactions.filter(transaction => {
            return transaction.description.toLowerCase().includes(this.description.toLowerCase());
        });
        },
    elementsFilteredForDate() {
            return this.transactions.filter(transaction => {
                return transaction.date.includes(this.startDate);
            });
     },
  } */
        
	
}).mount('#app')