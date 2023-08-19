<template>
    <div>
        <el-card shadow="never" class="index">
            <template #header>
                <div class="card_header">
                    <b>控制台</b>
                </div>
            </template>
        </el-card>
    </div>
</template>

<script>

export default {
    components: { },
    setup() {
        return {};
    }
};
</script>

<style lang="scss" scoped>
.card_header {
    display: flex;
    align-items: center;
    justify-content: space-between;
}
</style>
