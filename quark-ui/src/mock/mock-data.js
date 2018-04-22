// require('mockjs/dist/mock-min')

const Mock = require('mockjs/dist/mock');
// 获取 mock.Random 对象
const Random = Mock.Random;
// mock一组数据
const produceNewsData = function () {
  console.log("rurl 1 is " + this.rurl);
  return generateData();
};

const produceNewsData2 = function () {
  console.log("rurl 2 is " + this.rurl);
  return generateData();
};

function generateData() {
  let data = [];
  let pageSize = 20;
  for (let i = 0; i < pageSize; i++) {
    let newArticleObject = {
      title: Random.csentence(5, 30), //  Random.csentence( min, max )
      // thumbnail_pic_s: Random.dataImage('300x250', 'mock的图片'), // Random.dataImage( size, text ) 生成一段随机的 Base64 图片编码
      author_name: Random.cname(), // Random.cname() 随机生成一个常见的中文姓名
      date: Random.date() + ' ' + Random.time() // Random.date()指示生成的日期字符串的格式,默认为yyyy-MM-dd；Random.time() 返回一个随机的时间字符串
    };
    data.push(newArticleObject)
  }

  let pagination = {totalCount: 300, currentPageNo: 1, pageSize: pageSize, sort: null, sortColumnName: null};

  console.log("generate data size is {}", data.length);

  return {
    data: data,
    pagination: pagination
  }
}

// &currentPageNo=0 404 (Not Found)

Mock.mock(/\/news\/list\?userName=\w+&currentPageNo=\d+/, produceNewsData2);

Mock.mock(/\/news\/list\?currentPageNo=\d+/, produceNewsData);

Mock.mock(/\/news\/list2\?userName=\w+&currentPageNo=\d+/, produceNewsData2);

Mock.mock(/\/news\/list2\?_=\d+&userName=\w+&currentPageNo=\d+/, produceNewsData2);


// jQuery.ajax({
//   url: '/news/list?currentPageNo=1',
// }).done(function (data, status, xhr) {
//   let res = JSON.parse(data);
//   console.log("data first is " + JSON.stringify(res.data[0]))
// });

