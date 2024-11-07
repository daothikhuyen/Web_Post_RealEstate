import { createRouter, createWebHistory} from "vue-router"

import HomeComponent from '../components/HomeComponent.vue';
import LoginComponent from '../components/Form/LoginComponent.vue';
import SignUpComponent from '../components/Form/SignUpComponent.vue';


const routes = [
    {
        name: 'home',
        path: '/home',
        component: HomeComponent,
        meta: { public: true }
    },
    {
        name: 'login',
        path: '/login',
        component: LoginComponent,
        meta: { public: true }
    },
    {
        name: 'signup',
        path: '/signup',
        component: SignUpComponent,
        meta: { public: true }
    },
    
]

const router = createRouter({
    history: createWebHistory(),
    routes
})


router.beforeEach((to, from, next) => {
    // Check if the user is authenticated (e.g., check if a token is stored)
    const isAuthenticated = !!localStorage.getItem('KEY_TOKEN');

    if (!to.meta.public && !isAuthenticated) {
    // If the route is not public and the user is not authenticated, redirect to login
        next({ name: 'login' });
    }
    else {
        // Otherwise, allow navigation
        next();
    }
});

export default router