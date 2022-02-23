import Vue from 'vue';
import Router from 'vue-router';

Vue.use(Router);

export default new Router({
    routes: [
        {
            path: '/',
            redirect: '/login'
        },
        {
            path: '/admin',
            redirect: '/admin/cs'
        },
        {
            path: '/admin',
            component: () => import('../components/common/Home.vue'),
            meta: { title: ' ' },
            children: [
                {
                    path: '/admin/cs',
                    component: () => import( '../components/page/cs.vue'),
                    meta: { title: '计科学硕' }
                },
                {
                    path: '/admin/wa',
                    component: () => import('../components/page/wa.vue'),
                    meta: { title: '网安学硕' }
                }
            ]
        },
        {
            path: '/login',
            component: () => import('../components/page/Login.vue'),
            meta: { title: '登录' }
        },
        {
            path: '/register',
            component: () => import('../components/page/Register.vue'),
            meta: { title: '注册' }
        }
    ]
});
