<template>
  <div 
    class="overlay"
    @drop="onhideOverlay($event)"
    @dragover.prevent
    @dragenter.prevent
>
    <div 
        class="trash-container"
        @drop="onDrop($event)"
        @dragover.prevent
        @dragenter.prevent
    >
        <b-row>
            <b-col>
                <div class="trash-can">
                    <template>
                        <div class="text-center m-3">
                            <b-icon icon="trash" class="text-secondary" style="width: 40px; height: 40px;"></b-icon>
                            <p class="text-secondary">Eliminar</p>
                        </div>
                    </template>
                </div>
            </b-col>
        </b-row>
    </div>
  </div>
</template>

<script>
import movieService from "@/services/Movie"
export default {
    name: 'DeleteComponent',
    props:{
        onDrag:{
            type: Boolean,
            required: true
        }
    },
    data(){
        return{
            isMounted: false
        }
    },
    methods:{
        onDrop(evt) {
            const itemID = evt.dataTransfer.getData("movieId")
            this.$swal.fire({
                title: '¿Eliminar película permanentemente?',
                text: "Si la eliminas no podrás recuperarla",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Sí, eliminar',
                cancelButtonText: 'Cancelar'
            }).then(async (result)=>{
                if(result.isConfirmed){
                    const {status} = await movieService.deleteMovie(itemID)
                    if (status === 200) {
                        this.$swal.fire({
                            title: "¡Eliminación exitosa!",
                            icon: "success",
                            showCancelButton: false,
                            showConfirmButton: false,
                            timer: 3000
                        }).then(() =>{
                            this.$emit('getMovies')
                        })
                    }
                }
            })
        },
        onhideOverlay(evt){
            const indexCard = evt.dataTransfer.getData("indexCard");
            document.getElementsByClassName('card-style')[indexCard].style['z-index'] = 1
            this.$emit('showOverlay')
        },
        showInfo(){
            const Toast = this.$swal.mixin({
                toast: true,
                position: "top-end",
                showConfirmButton: false,
                timer: 3000,
                timerProgressBar: true,
                didOpen: (toast) => {
                    toast.onmouseenter = this.$swal.stopTimer;
                    toast.onmouseleave = this.$swal.resumeTimer;
                }
            });
            Toast.fire({
                icon: "success",
                title: "Signed in successfully"
            });

        }
    }
}
</script>
.
<style scoped>
.overlay{
    width: 100%;
    transition: 1.3s;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.5);
    position: fixed;
    z-index: 98;
    padding: 0;
    margin: 0;
    display: flex;
}
.trash-container{
    position: absolute;
    bottom: 100px;
    width: 150px;
    height: 100px;
    background-color: white;
    border-radius: 10px;
    left: 728px;
}
.trash-container:hover{
    box-shadow: 2px -2px 58px 15px rgb(255, 247, 247) !important;
    transition: 0.5s;
    cursor: pointer;
}

.trash-can{
    top: 100px;
    display: flex;
    justify-content: center;
    height: 100%;
    width: 100%;
}
</style>