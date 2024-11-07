// HomeComponent.vue

<template>
    <div class="HomeComponent">
        <navbar-component></navbar-component>
        <div class="view_home container-fluid mt-3" style="">
            xin chào
        </div>
    </div>
</template>

<script>
import { ref, defineComponent } from 'vue'
import NavbarComponent from './NavbarComponent.vue'

import axios from 'axios'

export default defineComponent(
    {
        name: "HomeComponent",
        components: {
            NavbarComponent,
        },
    }
)


</script>

<style scoped>

    .view_home .advertise:nth-child(1) {
        padding: 0;
        display: flex;
        justify-content: end;
    }

    .view_home .advertise .advertisement{
        position: fixed;
        /* width: 200px; */
        height: 600px;
        overflow: auto;
        padding-top: 10px;
        float: end;
    }

    .App .view_home .advertise .advertisement img{
        width: 100%;
        height: 100%;
        padding-top: 15%;
        padding-bottom: 20%;

    }

    .App.fix_body .view_home .advertise .advertisement img{
        width: 100%;
        height: 100%;
        padding-top: 30%;
        padding-bottom: 0%;
    }

    /* .view_home .advertise:nth-child(1) .advertisement img{

    } */

    .HomeComponent{
        position: relative;
    }

    .view_main .parent_header_search{
        background-color: #fff;
        padding: 10px;
        position: relative;
    }

    .view_main .header_search{
        width: 100%;
        height: 48px;
        background-color: #F2F2F2;
        border: 1px solid #F2F2F2;
        border-radius: 8px;
        padding: 6px 0px;
        display: flex;
        align-items: center;
    }

    .view_main .header_search .searchLocation{
        cursor: pointer;
        float: left;
        width: 200px !important;
    }

    .view_main .header_search .searchLocation .listsearch{
        position: relative;
        color: #2C2C2C;
        /* display: flex;
        align-items: center; */
    }

    .searchLocation .listsearch .icon_location {
        float: left;
        margin: 5px 0px 4px 12px;
    }

    .searchLocation .listsearch .name_search{
        font-size: 14px;
        line-height: 20px;
        float: left;
        color: rgb(44, 44, 44);
        font-family: "Lexend Medium", Roboto, Arial !important;
        font-weight: normal !important;
        padding: 2px 0px;
        margin: 6px 0px 5px 8px;
    }

    .searchLocation .listsearch .fa-solid.fa-chevron-down{
        float: right;
        padding: 4px 0px 4px 0px;
        margin: 4px 12px 4px 12px;
        color: #999;
        transition: transform .4s ease;
    }

    .searchLocation .listsearch span.postfix-div{
        position: absolute;
        content: "";
        display: block;
        width: 0px;
        height: 30px;
        left: calc(100% + 2px);
        top: 4px;
        border-width: 1px;
        border-style: solid;
        border-color: rgb(204, 204, 204);
        border-image: initial;
    }

    .view_main .header_search i.bi.bi-search{
        float: left;
        text-align: center;
        margin: auto;
        font-size: 18px;
        padding: 7px 7px 7px 15px;
    }

    .view_main .header_search .LocationInputSearch{
        float: left;
        display: block;
        flex: 1 0 auto;
    }

    .view_main .header_search .LocationInputSearch input.input_search{
        width: 100%;
        height: 32px;
        display: block;
        outline: none;
        border: none;
        background-color: transparent;
    }

    .view_main .header_search button.btnSearch{
        float: right;
        margin: 0 7px;
        padding: 6px 13px;
        max-height: 32px;
        max-width: 90px;
        flex: 1 0 auto;
        display: flex;
        align-items: center;
        color: #fff;
        background: #ff5d26;
        border: solid 1px #ff5d2631;
        font-family: "Lexend Medium", Roboto, Arial !important;
        font-size: 14px;
        line-height: 20px;
        font-weight: normal !important;
        letter-spacing: -.2px;
        padding: 5px 11px;
        border-radius: 4px;
    }

    .view_main .header_search button.btnSearch:hover{
        background-color: rgb(255, 93, 38,0.7);
    }

    /* đề xuất tìm kiếm theo nội dung */
    form .listItemProps{
        max-height: 380px;
        display: none;
        position: absolute;
        background-color: #fff;
        width: 100%;
        margin-top: 10px;
        padding: 8px 0px;
        box-shadow: 0px 2px 4px -1px rgba(0, 0, 0, 0.2),
                    0px 4px 5px 0px rgba(0, 0, 0, 0.14),
                    0px 1px 10px 0px rgba(0, 0, 0, 0.12);
        overflow: hidden;
        z-index: 7;
    }

    form .listItemProps.show{
        display: block;
    }

    form .listItemProps .conntent_search{
       padding: 5px 12px 8px 12px;
    }

    form .listItemProps .ul_listItem .li_listItem{
        padding: calc(2px + 4px) 12px;
        /* color: #8C8C8C; */
        font-size: 0.875rem;
        cursor: pointer;
        transition: background-color 150ms cubic-bezier(0.4,0,0.2,1) 0ms;
        -webkit-box-orient: vertical;
        -webkit-line-clamp: 1;
        display: -webkit-box;
        overflow: hidden;
    }

    form .listItemProps .ul_listItem .li_listItem:hover{
        background-color: #E8E8E8 ;
    }

    .info {
        width: 100vw;
        height: 100vh;
        position: absolute;
        top: 27%;
        left: 0;
        right: 0;
        z-index: 9999;
        text-align: center;
        animation: slideInTop ease .2s, fadeOut linear 1s 2s forwards;
    }

    @keyframes slideInTop {
        from {
            opacity: 0;
            /* transform: translateY(calc(100% + 32px)); */
        }
        to{
            opacity: 1;
            /* transform: translateY(0); */
        }
    }

    @keyframes fadeOut {
        to{
            opacity: 0;
        }
    }

    .box_search_locations {
        display: none;
        position: fixed;
        z-index: 99999;
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
        margin: auto;
        width: 30px;
        height: 30px;
        border: 3.5px solid #45474b;
        border-radius: 50%;
        border-style:  dotted ;
        animation: spin 2.5s infinite linear;
    }

    /* responsive */
    @media only screen and (max-width: 1200px) {
        .view_home .advertise .advertisement {
            display: block;
            position: fixed;
            width: 150px;
            height: 600px;
            overflow: auto;
            padding-top: 10px;
        }

        .view_home .main_home .list-group-search .list-group-item a {
            font-size: 13px;
        }
    }

    @media only screen and (max-width: 992px) {
        .view_home .advertise .advertisement {
            display: none;
            position: fixed;
            width: 150px;
            height: 600px;
            overflow: auto;
            padding-top: 10px;
        }

        .view_home .main_home .list-group-search .list-group-item a {
            font-size: 14px;
        }
    }

    @media only screen and (max-width: 768px) {
        .view_home .main_home .search-acreage,
        .view_home .main_home .search-price {
            max-height: 58px;
            overflow: hidden;
        }
    }

    @media only screen and (max-width: 600px) {

        .view_home .view_main .the_right{
            display: none;
        }

        .view_main .header_search .searchLocation{
            max-width: 100px;
        }

        .view_main .header_search i.bi.bi-search,
        .searchLocation .listsearch .name_search{
            font-size: 12px;
        }

        .searchLocation .listsearch .fa-solid.fa-chevron-down{
            display: none;
        }

        .swiper-slide img {
            height: auto;
        }
    }

    @media only screen and (max-width: 477px) {
        .container-fluid{
            padding-left: 0px;
            padding-right: 0px;
        }

        .row>* {
            flex-shrink: 0;
            width: 100%;
            max-width: 100%;
            padding: 0px;
            margin-top: var(--bs-gutter-y);
        }

        .view_main .header_search .searchLocation{
            max-width: 40px;
        }

        .searchLocation .listsearch .name_search{
            display: none;
        }

        .searchLocation .listsearch .icon_location{
            margin: 5px 0px 4px 14px;
        }

        .view_main .header_search .LocationInputSearch input{
            font-size: 10px;
        }

        .view_main .header_search .LocationInputSearch input.input_search{
            font-size: 15px;
        }

        .view_main .header_search button.btnSearch{
            font-size: 10px;
            max-height: 25px;
            max-width: 65px;
        }

        .the_right {
            display: none;
        }

        .view_main .header_search button.btnSearch{
            display: none;
        }
    }
</style>
