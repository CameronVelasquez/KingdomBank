const {createApp} = Vue 

createApp({
	data(){
        return {
            account: [],
            dataAccounts: {},
            objectAccount: {},
            objectTransactions: [],
            objectClient: {}
            
        }

	},
	created(){
        this.cargarDatosCuenta()
        this.cargarDatosClx()
        
	},
    methods: {
        /* cargarDatosCuenta (){
            let url = new URLSearchParams(location.search)
            let id = url.get("id")
            axios.get(`http://localhost:8080/api/accounts/${id}`)
                .then(response =>{
                   this.dataAccounts = response
                   this.objectAccount = this.dataAccounts.data
                   this.objectTransactions = this.objectAccount.transactions.sort((a, b)=> new Date(b.date).getTime() - new Date(a.date).getTime())
                   console.log(this.dataAccounts)
                })
                .catch(error => console.log(error))
        }, */
        cargarDatosCuenta(){
            let id = new URLSearchParams(location.search).get("id")
            axios("http://localhost:8080/api/clients/current/accounts")
            .then(response =>{
                this.dataAccounts = response.data
                this.objectAccount = this.dataAccounts.find(account => account.id == id )
                this.objectTransactions = this.objectAccount.transactions.sort((a, b)=> new Date(b.date).getTime() - new Date(a.date).getTime())
                console.log(this.objectTransactions)
            })
            .catch(error => console.log(error))
        },
        cargarDatosClx(){
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
                location.href = "/web/index.html"
            })
            .catch(error => console.log(error))
        }
        
        }
        
	
}).mount('#app')