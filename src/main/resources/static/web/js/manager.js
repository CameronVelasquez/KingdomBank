
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
            axios.get("http://localhost:8080/clients")
            .then(response => {
            this.data = response,
            this.objectClients = this.data.data._embedded.clients
            
            })
            .catch(err => console.log(err))
        },
        addClient(){
            if (this.validateMail()){
                axios.post("http://localhost:8080/clients", this.newClient)
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
            axios.delete(client._links.client.href)
            .then(response =>{
                this.loadData()
                
                
            })
        }
   
    }
	
}).mount('#app')