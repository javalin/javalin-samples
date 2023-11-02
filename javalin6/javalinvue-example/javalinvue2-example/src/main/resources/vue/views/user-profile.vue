<template id="user-profile">
    <app-frame>
        <div v-if="user.loading">Loading user...</div>
        <div v-if="user.loadError">Error while loading user</div>
        <dl v-if="user.loaded">
            <dt>User ID</dt>
            <dd>{{ user.data.id }}</dd>
            <dt>Name</dt>
            <dd>{{ user.data.name }}</dd>
            <dt>Email</dt>
            <dd>{{ user.data.email }}</dd>
            <dt>Birthday</dt>
            <dd>{{ user.data.userDetails.dateOfBirth }}</dd>
            <dt>Salary</dt>
            <dd>{{ user.data.userDetails.salary }}</dd>
        </dl>
    </app-frame>
</template>
<script>
Vue.component("user-profile", {
    template: "#user-profile",
    data: () => ({
        user: new LoadableData(`/api/users/${JavalinVue.pathParams["user-id"]}`),
    }),
});
</script>
