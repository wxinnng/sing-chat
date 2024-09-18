import {ref} from "vue";
import {message} from "@/util/Utils.js";


//get message
const getMessage = () => {
    socket.value.onmessage = (msg) => {
        console.log(msg)
    }
}
//destroy a socket connection
const destroySocketConnection = (socket) => {
    socket.value.addEventListener('close',() => {
        console.log('Connection closed')
    })
    socket.value = null;
}
//post a message to server
const postMessageToServer = (message,socket) => {
    socket.value.send(JSON.stringify(message))
}
export  {
    getMessage,
    postMessageToServer,
    destroySocketConnection,
}