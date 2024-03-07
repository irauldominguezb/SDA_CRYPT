<template>
    <div>
        <div class="container">
            <b-card
                class="shadow-lg mt-3"
            >
                <template slot="header" >
                    <b-row class="pt-1">
                        <b-col lg="12">
                            <h5 class="text-secondary text-center">
                                {{ foundMovie.name }}
                            </h5>
                        </b-col>
                    </b-row>
                </template>
                <b-row>
                    <b-col lg="12" class="mb-3">
                        <b-button
                            variant="primary"
                            to="/"
                            class="float-right"
                        >
                            <b-icon icon="arrow-left"></b-icon>
                            Volver
                        </b-button>
                    </b-col>
                </b-row>
                <div class="container pt-3">
                    <b-row>
                        <b-col cols="12" lg="8" v-if="foundMovie.id">
                            <b-row>
                                <b-col cols="12" class="mt-2 d-flex justify-content-between">
                                    <h5>
                                Director: <span class="text-secondary">{{foundMovie.director}}</span>
                                    </h5>
                                </b-col>
                            </b-row>
                            <b-row>
                                <b-col cols="12" class="mt-2">
                                    <h5>
                                Publicación: <span class="text-secondary">{{foundMovie.publication.split('T')[0]}}</span>
                                    </h5>
                                </b-col>
                            </b-row>
                            <b-row>
                                <b-col cols="12" class="mt-2">
                                    <h5>
                                Género: <span class="text-secondary">{{foundMovie.gender.name}}</span>
                                    </h5>
                                </b-col>
                            </b-row>
                        </b-col>
                        <b-col cols="12" lg="4" class="text-end">
                            <b-img
                                fluid
                                thumbnail
                                src="https://via.placeholder.com/260"
                                alt="Image"
                                rounded
                            ></b-img>
                        </b-col>
                    </b-row>
                </div>
            </b-card>
        </div>
    </div>
</template>

<script>
import services from "@/services/Movie"
export default {
    name: 'MovieDetail',
    data(){
        return{
            foundMovie:{}
        }
    },
    methods: {
        async getMovieById(){
            const id = this.$route.params.id
            const {status, data} = await services.getMovieById(id)
            if(status === 200){
                this.foundMovie = data
            }
        }
    },
    mounted(){
        this.getMovieById()
    }
}
</script>

<style>

</style>