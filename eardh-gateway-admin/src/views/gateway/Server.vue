<template>
    <div>
        <PageDataView :="page"
                      @addData="form = {}"
                      @pageChange="handlePageChange"
                      @handleEdit="handleEdit"
                      @formSubmit="submit">
            <template v-slot:tableSlot>
                <el-table-column prop="gatewayId" label="网关标识" />
                <el-table-column prop="groupName" label="分组名称" />
                <el-table-column prop="gatewayName" label="网关名称" />
                <el-table-column prop="gatewayDescription" label="网关描述" />
            </template>

            <template v-slot:buttonSlot>
                <el-table-column fixed="right" label="在线实例查询" width="120">
                    <template #default="scope">
                        <el-button type="primary" size="small" :icon="Edit"
                                   color="#95d475"
                                   @click="handleInstance(scope.$index, scope.row)"/>
                    </template>
                </el-table-column>
            </template>

            <template v-slot:formSlot>
                <el-form
                    label-position="right"
                    label-width="100px"
                    :model="form"
                    style="max-width: 460px"
                >
                    <el-form-item label="网关标识">
                        <el-input disabled v-model="form.gatewayId"/>
                    </el-form-item>
                    <el-form-item label="网关名称">
                        <el-input v-model="form.gatewayName"/>
                    </el-form-item>
                    <el-form-item label="分组名称">
                        <el-input v-model="form.groupName"/>
                    </el-form-item>
                    <el-form-item label="网关描述">
                        <el-input v-model="form.gatewayDescription"/>
                    </el-form-item>
                </el-form>
            </template>
        </PageDataView>

        <el-dialog
            v-model="instanceDialogVisible"
            title="编辑"
            width="30%"
            destroy-on-close
            center
        >

            <el-table :data="instanceData" border cell-style="" stripe style="width: 100%">
                <el-table-column width="120" prop="ip" label="服务IP地址"/>
                <el-table-column width="100" prop="port" label="服务端口号"/>
                <el-table-column width="100" prop="healthy" label="是否健康"/>
                <el-table-column width="100" prop="enable" label="是否可用"/>
            </el-table>

            <template #footer>
      <span class="dialog-footer">
        <el-button @click="instanceDialogVisible = false">取消</el-button>
      </span>
            </template>
        </el-dialog>
    </div>
</template>

<script setup>
import PageDataView from "@/components/PageDataView.vue";
import {
    fetchGatewayServerData,
    fetchOnlineApplicationInstanceData,
    fetchOnlineGatewayInstanceData,
    updateGatewayServerData
} from "@/api/apis";
import {ref} from "vue";
import {errorTip, successTip} from "@/utils/tool";
import {Edit} from "@element-plus/icons";

const page = {
    pageTitle: ref('网关列表'),
    currentPage: ref(1),
    pageSize: ref(10),
    total: ref(0),
    tableData: ref([])
}

const form = ref({
    gatewayId: String,
    groupName: String,
    gatewayName: String,
    gatewayDescription: String
})

const getTableData = (current, size) => {
    fetchGatewayServerData(current, size).then((res) => {
        let data = res.data;
        if (data.code == '0000') {
            data = data.data;
            page.total.value = data.total
            page.tableData.value = []
            page.tableData.value.push(...data.items)
        }
    })
}

getTableData(page.currentPage.value, page.pageSize.value)

const handlePageChange = (val) => {
    getTableData(val, page.pageSize.value)
}

const handleEdit = (index, row) => {
    form.value = row
}

const submit = () => {
    updateGatewayServerData(form.value).then(res => {
        let data = res.data;
        if (data.code == '0000') {
            successTip()
        } else {
            errorTip()
        }
        getTableData(page.currentPage.value, page.pageSize.value)
    })
}


const instanceData = ref([])

const instanceDialogVisible = ref(false)

const handleInstance = (index, row) => {
    fetchOnlineGatewayInstanceData(row.gatewayId).then((res) => {
        let data = res.data;
        if (data.code == '0000') {
            data = data.data;
            instanceData.value = []
            instanceData.value.push(...data)
        }
    })
    instanceDialogVisible.value = true
}

</script>

<style scoped></style>
