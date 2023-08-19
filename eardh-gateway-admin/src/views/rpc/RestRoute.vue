<template>
    <div>
        <PageDataView :="page"
                      @addData="form = {}"
                      @pageChange="handlePageChange"
                      @handleEdit="handleEdit"
                      @formSubmit="submit">
            <template v-slot:tableSlot>
                <el-table-column width="150" show-overflow-tooltip prop="id" label="Rest标识"/>
                <el-table-column width="150" show-overflow-tooltip prop="apiId" label="API标识"/>
                <el-table-column width="250" prop="microserviceName" label="应用名称"/>
                <el-table-column width="100" prop="microserviceGroup" label="应用所属组"/>
                <el-table-column width="100" show-overflow-tooltip prop="microserviceDescription" label="应用描述"/>
                <el-table-column width="150" prop="restUrl" label="url地址"/>
                <el-table-column width="80" prop="restMethod" label="rest类型"/>
                <el-table-column width="100" show-overflow-tooltip prop="restParametersType" label="参数类型"/>
                <el-table-column width="100" show-overflow-tooltip prop="restParametersName" label="参数名"/>
                <el-table-column width="100" show-overflow-tooltip prop="restResultType" label="返回值类型"/>
                <el-table-column width="100" prop="restDescription" label="rest描述"/>
            </template>

            <template v-slot:buttonSlot>
                <el-table-column fixed="right" label="路由调试" width="120">
                    <template #default="scope">
                        <el-button :disabled="scope.row.apiId == null || scope.row.apiId == ''" type="primary" size="small" :icon="Aim"
                                   color=" #f3d19e"
                                   @click="doDebugger(scope.$index, scope.row)"/>
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
                    <el-form-item label="Rest标识">
                        <el-input disabled v-model="form.id"/>
                    </el-form-item>
                    <el-form-item label="网关API标识">
                        <el-input v-model="form.apiId"/>
                    </el-form-item>
                    <el-form-item label="应用名称">
                        <el-input v-model="form.microserviceName"/>
                    </el-form-item>
                    <el-form-item label="应用所属组">
                        <el-input v-model="form.microserviceGroup"/>
                    </el-form-item>
                    <el-form-item label="应用描述">
                        <el-input v-model="form.microserviceDescription"/>
                    </el-form-item>
                    <el-form-item label="url地址">
                        <el-input v-model="form.restUrl"/>
                    </el-form-item>
                    <el-form-item label="rest方法">
                        <el-input v-model="form.restMethod"/>
                    </el-form-item>
                    <el-form-item label="参数类型">
                        <el-input v-model="form.restParametersType"/>
                    </el-form-item>
                    <el-form-item label="参数名">
                        <el-input v-model="form.restParametersName"/>
                    </el-form-item>
                    <el-form-item label="返回值类型">
                        <el-input v-model="form.restResultType"/>
                    </el-form-item>
                    <el-form-item label="rest描述">
                        <el-input v-model="form.restDescription"/>
                    </el-form-item>
                </el-form>
            </template>
        </PageDataView>

        <!--   调试弹窗     -->
        <el-dialog
            v-model="debugDialogVisible"
            title="调试"
            width="30%"
            @close="debuggerForm.result = ''"
            destroy-on-close
            center
        >

            <el-form
                label-position="right"
                label-width="100px"
                :model="debuggerForm"
                style="max-width: 460px"
            >
                <el-form-item label="API标识">
                    <el-input disabled v-model="debuggerForm.apiId"/>
                </el-form-item>
                <el-form-item label="API路径">
                    <el-input v-model="debuggerForm.apiPath"/>
                </el-form-item>
                <el-form-item label="API方法">
                    <el-input v-model="debuggerForm.apiMethod"/>
                </el-form-item>
                <el-form-item label="请求头">
                    <div>
                        <el-row :gutter="20" v-for="(header, index) in debuggerForm.headers">
                            <el-col :span="8">
                                <el-input placeholder="参数名" v-model="header.name"/>
                            </el-col>
                            -
                            <el-col :span="10">
                                <el-input placeholder="参数值" v-model="header.value"/>
                            </el-col>
                            <el-col :span="3">
                                <el-button @click="removeHeaderItem(index)" type="primary" size="small" color="#fcd3d3" :icon="Minus" circle />
                            </el-col>
                        </el-row>
                        <el-button @click="addHeaderItem" type="primary" size="small" color="#a0cfff" :icon="Plus" circle />
                    </div>
                </el-form-item>
                <el-form-item label="请求参数">
                    <div>
                        <el-row :gutter="20" v-for="(param, index) in debuggerForm.params">
                            <el-col :span="8">
                                <el-input placeholder="参数名" v-model="param.name"/>
                            </el-col>
                            -
                            <el-col :span="10">
                                <el-input placeholder="参数值" v-model="param.value"/>
                            </el-col>
                            <el-col :span="3">
                                <el-button @click="removeParamItem(index)" type="primary" size="small" color="#fcd3d3" :icon="Minus" circle />
                            </el-col>
                        </el-row>
                        <el-button @click="addParamItem" type="primary" size="small" color="#a0cfff" :icon="Plus" circle />
                    </div>
                </el-form-item>
                <el-form-item label="请求体" v-if="debuggerForm.apiMethod == 'post' || debuggerForm.apiMethod == 'patch' || debuggerForm.apiMethod == 'put'">
                    <div>
                        <el-radio-group v-model="debuggerForm.contentType">
                            <el-radio label="formType">form-data</el-radio>
                            <el-radio label="rowType">row</el-radio>
                        </el-radio-group>
                        <div v-if="debuggerForm.contentType == 'formType'">
                            <el-row :gutter="20" v-for="(form, index) in debuggerForm.formBody">
                                <el-col :span="8">
                                    <el-input placeholder="参数名" v-model="form.name"/>
                                </el-col>
                                -
                                <el-col :span="10">
                                    <el-input placeholder="参数值" v-model="form.value"/>
                                </el-col>
                                <el-col :span="3">
                                    <el-button @click="removeFormBodyItem(index)" type="primary" size="small" color="#fcd3d3" :icon="Minus" circle />
                                </el-col>
                            </el-row>
                            <el-button style="alignment-baseline: baseline" @click="addFormBodyItem" type="primary" size="small" color="#a0cfff" :icon="Plus" circle />
                        </div>
                        <el-input v-else type="textarea" v-model="debuggerForm.rowBody"/>
                    </div>
                </el-form-item>
                <div style="width: 100%">
                    <el-button @click="sendRequest" style="width: 50%; margin-left: 25%" type="primary" plain>请  求</el-button>
                </div>

                <div style="margin-top: 10px">
                    <VueJsonPretty :data="debuggerForm.result" :theme="'summerfruit'"></VueJsonPretty>
                </div>
            </el-form>
            <template #footer>
      <span class="dialog-footer">
        <el-button @click="debugDialogVisible = false">取消</el-button>
      </span>
            </template>
        </el-dialog>
    </div>
</template>

<script setup>
import VueJsonPretty from 'vue-json-pretty'
import 'vue-json-pretty/lib/styles.css';
import PageDataView from "@/components/PageDataView.vue";
import {doRequestDebugger, fetchGatewayApiByApiId, fetchRestRouteData, updateRestRouteData} from "@/api/apis";
import {ref} from "vue";
import {errorTip, successTip} from "@/utils/tool";
import {Aim, Minus, Plus} from "@element-plus/icons";

const page = {
    pageTitle: ref('Rest接口服务表'),
    currentPage: ref(1),
    pageSize: ref(10),
    total: ref(0),
    tableData: ref([])
}

const form = ref({
    id: String,
    apiId: String,
    microserviceId: String,
    microserviceName: String,
    microserviceGroup: String,
    microserviceDescription: String,
    restUrl: String,
    restMethod: String,
    restParametersType: String,
    restParametersName: String,
    restResultType: String,
    restDescription:String
})

const getTableData = (current, size) => {
    fetchRestRouteData(current, size).then((res) => {
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
    updateRestRouteData(form.value).then(res => {
        let data = res.data;
        if (data.code == '0000') {
            successTip()
        } else {
            errorTip()
        }
        getTableData(page.currentPage.value, page.pageSize.value)
    })
}



const debugDialogVisible = ref(false)
const debuggerForm = ref({
    apiId: String,
    apiPath: String,
    apiMethod: String,
    headers: [{
        name: String,
        value: String
    }],
    params: [{
        name: String,
        value: String
    }],
    contentType: String,
    formBody: [{
        name: String,
        value: String
    }],
    rowBody: String,
    result: ''
})

const doDebugger = (index, row) => {
    debuggerForm.value.apiId = row.apiId;
    debuggerForm.value.formBody = []
    debuggerForm.value.params = []
    debuggerForm.value.headers = []
    debuggerForm.value.rowBody = ''
    if (row.apiId != null || row.apiId != '') {
        fetchGatewayApiByApiId(row.apiId).then((res) => {
            let data = res.data
            if (data.code == '0000') {
                debuggerForm.value.apiPath = data.data.apiPath
                debuggerForm.value.apiMethod = data.data.apiMethod
                if (data.data.apiAuth == '1') {
                    debuggerForm.value.headers.push({
                        name: 'token',
                        value: ''
                    })
                }
            }
            debugDialogVisible.value = true
            let names = []
            if (row.restParametersName != null && row.restParametersName != '') {
                names.push(...(row.restParametersName.split(',')))
            }
            if (data.data.apiMethod == "post"
                || data.data.apiMethod == "patch"
                || data.data.apiMethod == "put") {
                names.forEach(name => {
                    debuggerForm.value.contentType = 'formType'
                    debuggerForm.value.formBody.push({
                        name: name,
                        value: ''
                    })
                })
            } else {
                names.forEach(name => {
                    debuggerForm.value.params.push({
                        name: name,
                        value: ''
                    })
                })
            }
        })
    }
}

const addHeaderItem = () => {
    debuggerForm.value.headers.push({
        name: '',
        value: ''
    })
}
const removeHeaderItem = (index) => {
    debuggerForm.value.headers.splice(index, 1)
}

const addParamItem = () => {
    debuggerForm.value.params.push({
        name: '',
        value: ''
    })
}
const removeParamItem = (index) => {
    debuggerForm.value.params.splice(index, 1)
}

const addFormBodyItem = () => {
    debuggerForm.value.formBody.push({
        name: '',
        value: ''
    })
}
const removeFormBodyItem = (index) => {
    debuggerForm.value.formBody.splice(index, 1)
}

const sendRequest = () => {
    doRequestDebugger(debuggerForm.value.apiPath, debuggerForm.value.apiMethod, debuggerForm.value.headers, debuggerForm.value.params, debuggerForm.value.formBody, debuggerForm.value.rowBody, debuggerForm.value.contentType)
        .then(res => {
            debuggerForm.value.result = res.data
        })
}

</script>

<style scoped></style>
