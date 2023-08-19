<template>
    <div>
        <PageDataView :="page"
                      @addData="form = {}"
                      @pageChange="handlePageChange"
                      @handleEdit="handleEdit"
                      @formSubmit="submit">
            <template v-slot:tableSlot>
                <el-table-column prop="microserviceId" label="微服务标识"/>
                <el-table-column width="250" prop="microserviceName" label="微服务名称"/>
                <el-table-column show-overflow-tooltip prop="microserviceDescription" label="微服务描述"/>
                <el-table-column prop="microserviceGroup" label="微服务分组名称"/>
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
                    <el-form-item label="微服务标识">
                        <el-input disabled v-model="form.microserviceId"/>
                    </el-form-item>
                    <el-form-item label="微服务名称">
                        <el-input v-model="form.microserviceName"/>
                    </el-form-item>
                    <el-form-item label="微服务描述">
                        <el-input v-model="form.microserviceDescription"/>
                    </el-form-item>
                    <el-form-item label="微服务分组名称">
                        <el-input v-model="form.microserviceGroup"/>
                    </el-form-item>
                </el-form>
            </template>
        </PageDataView>


        <el-dialog
            v-model="instanceDialogVisible"
            title="编辑"
            width="40%"
            destroy-on-close
            center
        >

            <el-table :data="instanceData" border cell-style="" stripe style="width: 100%">
                <el-table-column width="120" prop="ip" label="服务IP地址"/>
                <el-table-column width="100" prop="port" label="服务端口号"/>
                <el-table-column width="100" prop="weight" label="实例权重"/>
                <el-table-column width="100" prop="healthy" label="是否健康"/>
                <el-table-column width="100" prop="enable" label="是否可用"/>
                <el-table-column width="100" prop="version" label="实例版本号"/>
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
    fetchApplicationData,
    fetchOnlineApplicationInstanceData,
    updateApplicationData,
    updateGatewayApiData
} from "@/api/apis";
import {ref} from "vue";
import {errorTip, formatter, successTip} from "@/utils/tool";
import {Edit} from "@element-plus/icons";

const page = {
    pageTitle: ref('微服务列表'),
    currentPage: ref(1),
    pageSize: ref(10),
    total: ref(0),
    tableData: ref([])
}

const form = ref({
    microserviceId: String,
    microserviceName: String,
    microserviceDescription: String,
    microserviceGroup: String,
})

const getTableData = (current, size) => {
    fetchApplicationData(current, size).then((res) => {
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
    updateApplicationData(form.value).then((res) => {
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
    fetchOnlineApplicationInstanceData(row.microserviceId).then((res) => {
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
