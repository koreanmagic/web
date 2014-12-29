/**
 * 
 */
/**
 * @author Administrator
 *
 */

/*@SqlResultSetMapping(
name="biz",
entities={@EntityResult(entityClass=BizValue.class,
						fields = {
        							@FieldResult(name="id", column = "id"),
        							@FieldResult(name="name", column = "biz_class"),
								}
						)
		},
columns = { @ColumnResult(name = "id"),
			@ColumnResult(name = "biz_class")
}
)*/

/*@org.hibernate.annotations.NamedNativeQueries({
	@NamedNativeQuery(
			name = "testcall",
			query = "{call test_call(:in, ?)}",
			callable=true,
			resultClass=BizClass.class,
			resultSetMapping=""
			),
	@NamedNativeQuery(
			name="biz_class",
			query = "SELECT * FROM biz_class",
			resultSetMapping="result"
			)
})*/

package kr.co.koreanmagic.hibernate3.mapper.domain;


/*
@SqlResultSetMapping(name="carkey",
entities=@EntityResult(entityClass=Car.class,
fields = {
        @FieldResult(name="id", column = "id"),
        @FieldResult(name="name", column = "name"),
        @FieldResult(name="dimension.length", column = "length"),
        @FieldResult(name="dimension.width", column = "width")
       }),
       columns = { @ColumnResult(name = "area")})
*/
