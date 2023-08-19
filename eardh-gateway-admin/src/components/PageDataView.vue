<template>
    <div>
        <el-card shadow="never" class="index">
            <template #header>
                <div class="card_header">
                    <b> {{ pageTitle }} </b>
                    <el-button type="primary" @click="addData" style="margin-right: 100pt" plain>添加数据</el-button>
                </div>
            </template>

            <el-table :data="tableData" border cell-style="" stripe style="width: 100%">

                <slot name="tableSlot"></slot>

                <el-table-column width="100" :formatter="formatter" prop="createTime" label="创建时间"/>
                <el-table-column width="100" :formatter="formatter" prop="updateTime" label="更新时间"/>

                <el-table-column fixed="right" label="Edit" width="60">
                    <template #default="scope">
                        <el-button type="primary" size="small" :icon="Edit" color="#a0cfff" circle @click="handleEdit(scope.$index, scope.row)"/>
                    </template>
                </el-table-column>
                <slot name="buttonSlot"></slot>
            </el-table>

            <el-pagination
                    class="pagination"
                    v-model:current-page="currentPage"
                    v-model:page-size="pageSize"
                    background
                    small
                    layout="prev, pager, next"
                    :total="total"
                    @current-change="handleCurrentChange"
            />
        </el-card>

        <el-dialog
                v-model="centerDialogVisible"
                title="编辑"
                width="30%"
                destroy-on-close
                center
        >
            <slot name="formSlot"></slot>

            <template #footer>
      <span class="dialog-footer">
        <el-button @click="centerDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submit">
          提交
        </el-button>
      </span>
            </template>
        </el-dialog>
    </div>
</template>

<script setup>

import {onMounted, ref} from "vue";
import {formatter} from "@/utils/tool";
import {Edit} from "@element-plus/icons";

const props = defineProps(
    ['pageTitle','currentPage', 'pageSize', 'total', 'tableData']
)

const emit = defineEmits(['pageChange', 'handleEdit', 'formSubmit', 'addData'])

const centerDialogVisible = ref(false);
const {pageTitle, currentPage, pageSize, total, tableData} = {
    pageTitle: props.pageTitle,
    currentPage: props.currentPage,
    pageSize: props.pageSize,
    total: props.total,
    tableData: props.tableData
}

const handleCurrentChange = (val) => {
    emit('pageChange', val)
}

const handleEdit = (index, row) => {
    centerDialogVisible.value = true
    emit('handleEdit', index, row)
}

const addData = () => {
    centerDialogVisible.value = true
    emit('addData');
}

const submit = () => {
    emit('formSubmit')
    centerDialogVisible.value = false
}

</script>

<style scoped>
.card_header {
    display: flex;
    align-items: center;
    justify-content: space-between;
}

.pagination {
    margin-top: 1vh;
}
</style>
