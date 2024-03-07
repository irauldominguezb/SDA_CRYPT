import VueRouter from "vue-router";
import Vue from "vue";
Vue.use(VueRouter);

const routes = [
    {
        path: "/",
        component:()=> import("../modules/movies/Movies.vue")
    },
    {
        path: "/movieDetail/:id",
        component:()=> import("../modules/movies/MovieDetail.vue")
    }
]
const router = new VueRouter({routes})
export default router;