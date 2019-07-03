$(function() {
	getMandate();
})

function getMandate() {

	let json = JSON.parse(document.getElementById("jsonData").value)
	for ( let i in json) {
		json[i].hour = toData(json[i].hour);
	}
	json.sort(function(a, b) {
		let val = a.hour.getTime() - b.hour.getTime()
		return val == 0 ? 1 : val;
	})
	var rejectRate =[];
	var cutValue = [];
	var cutTime = [];
	var average=[];
	var sum=0;
	var max = 0;
	console.log(json);
	for ( let i in json) {
		cutTime.push(formatDate(json[i].hour,"yyyy-MM-dd hh:mm"));
		cutValue.push(json[i].shareAccept /1000);
		if(json[i].shareAccept != 0){
			max  = json[i].shareAccept;
		}
		sum+=json[i].shareAccept/1000;
		rate=((json[i].rejectRate/1000)/((json[i].shareAccept/1000)+(json[i].rejectRate/1000)))*100;
		rejectRate.push(rate);
	}
	
	var ave= sum/json.length;
	for(let i = 0; i< json.length ;i++){
		average.push(ave);
	}
	if(max == 0){
		var minValue = 0;
		var maxValue=5;
	}
	
	
	function toData(hour) {
		let s = hour.toString();
		let y = s.substr(0, 2);
		let m = s.substr(2, 2);
		let d = s.substr(4, 2);
		let h = s.substr(6, 2);
		let dateStr = "20" + y + "-" + m + "-" + d + " " + h + ":" + "00" + ":"
				+ "00";
		return new Date(dateStr);
	}

	
	var myChart = echarts.init(document.getElementById('linChart'));
	var option = {
		color:['#169f85','#E8BD8F','#000000'],
		visualMap: [{
			show: false,
			type: 'continuous',
			seriesIndex: 1,
			dimension: 0,
			min: 0,
			max: cutTime.length + 1
		}],
		title: [{
			left: 'center'
		}],
		tooltip: {trigger: 'axis'},
		legend:{
			data:['小时平均算力(P)','算力(P)','小时拒绝率(%)']
		},
		xAxis: [{
			data: cutTime,
			axisTick: false,
			boundaryGap: false
		}],
		yAxis: [
			{
				name:'算力（P）',
				type:'value',
				position:'left',
				splitNumber:5,
				min: minValue,
				max: maxValue,
				splitLine: {
					show: false
				}
			}
				,{
					name:'拒绝率（%）',
					max:100,
					splitLine: {
						show: false
					}
				}
		],
		grid: [{top: '20%',bottom: '10%',right: '5%',left: '10%'}],
		series: [{
			name: '小时平均算力(P)',
			type: 'line',
			smooth: true,
			splitLine: {
				show: false
			},
			data: average
		},
		{
			name: '算力(P)',
			type: 'line',
			data: cutValue
		},
			{
				name: '小时拒绝率(%)',
				type: 'line',
				smooth: true,
				splitLine: {
					show: false
				},
				yAxisIndex: 1,
				data: rejectRate
			}
		]
	};
	myChart.setOption(option);
	myChart.resize();
	function formatDate(date, fmt) {
		if (/(y+)/.test(fmt)) {
			fmt = fmt.replace(RegExp.$1, (date.getFullYear() + '')
					.substr(4 - RegExp.$1.length));
		}
		let o = {
			'M+' : date.getMonth() + 1,
			'd+' : date.getDate(),
			'h+' : date.getHours(),
			'm+' : date.getMinutes(),
			's+' : date.getSeconds()
		};

		// 遍历这个对象
		for ( let k in o) {
			if (new RegExp(`(${k})`).test(fmt)) {
				// console.log(`${k}`)
				// console.log(RegExp.$1)
				let str = o[k] + '';
				fmt = fmt.replace(RegExp.$1, (RegExp.$1.length === 1) ? str
						: padLeftZero(str));
			}
		}
		return fmt;
	}
	;

	function padLeftZero(str) {
		return ('00' + str).substr(str.length);
	}
}