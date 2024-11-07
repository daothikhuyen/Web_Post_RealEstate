import { createApp } from 'vue';

import app from './App.vue'
import router from './router/index.js';
import store from './store/index.js'

const vueApp = createApp(app)

vueApp.use(router)
vueApp.use(store)
vueApp.mount('#app')

export default vueApp
