# 自动化测试数据存储目录

此目录用于存储自动化测试系统的数据文件（JSON格式）

## 目录结构

```
autotest-data/
├── testcases/          # 测试用例存储目录
│   └── *.json         # 各个接口的测试用例文件
├── results/           # 测试结果存储目录
│   └── *.json         # 测试执行结果文件
└── README.md          # 说明文档
```

## 数据格式

### 测试用例格式 (testcases/*.json)
```json
{
  "apiPath": "/business/allDataElement/getAllDataElementPage",
  "apiMethod": "POST",
  "controllerClass": "AlldataelementinfoController",
  "methodName": "getAllDataElementPage",
  "testCases": [
    {
      "id": "TC001",
      "description": "基础分页查询",
      "requestParams": {...},
      "createTime": "2025-11-03T10:00:00"
    }
  ]
}
```

### 测试结果格式 (results/*.json)
```json
{
  "testCaseId": "TC001",
  "executeTime": "2025-11-03T10:05:00",
  "requestParams": {...},
  "responseStatus": 200,
  "responseBody": {...},
  "responseTime": 150,
  "success": true,
  "errorMessage": null
}
```
