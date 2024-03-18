$('select[data-value]').each(function(index, el) {
	const $el = $(el);

	defaultValue = $el.attr('data-value').trim();

	if (defaultValue.length > 0) {
		$el.val(defaultValue);
	}
});

// 위 코드와 같다
//$('select[data-value2]').each(function(index, el) {
//        const $el = $(el);
//        const defaultValue = $el.data('value2'); //$el.data('data-value2')가 아닌 이유는 jQuery가 'data-'를 생략하기 때문
//        if (defaultValue) {
//            $el.val(defaultValue);
//        }
//    });
