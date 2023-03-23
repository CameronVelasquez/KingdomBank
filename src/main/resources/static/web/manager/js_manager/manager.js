
const {createApp} = Vue 

createApp({
	data(){
        return {
            data: [],
            objectClients: {},
            newClient: {firstName: "", lastName: "", email: ""},
            idLink: "",
            
        }

	},
	created(){
        this.loadData()
        
	},

    methods: {
        loadData() {
            axios.get("http://localhost:8080/api/clients")
            .then(response => {
            this.data = response,
            this.objectClients = this.data.data
            
            })
            .catch(err => console.log(err))
        },
        addClient(){
            if (this.validateMail()){
                axios.post("http://localhost:8080/rest/clients", this.newClient)
            .then( response => {
                this.loadData(),
                this.refreshForm()
            })
            }
        },
        refreshForm(){
            this.newClient = {firstName: "", lastName: "", email: ""}
        },
        validateMail() {
            return /^[a-zA-Z0-9.!#$%&'+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:.[a-zA-Z0-9-]+)$/.test(this.newClient.email)
        },
        deleteClient(client) {
            axios.delete("/rest/clients")
            .then(response =>{
                this.loadData()
                
                
            })
            .catch(error => {
                console.log(error)
            })
        }
   
    }
	
}).mount('#app')