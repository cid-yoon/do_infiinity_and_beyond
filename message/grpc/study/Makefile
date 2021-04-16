.PHONY: proto_before proto_after proto_format clean

BEFORE_PATH=productinfo

proto_before: proto_format
	protoc \
	-I=./productinfo \
	--go_out=:./productinfo/go/server/ecommerce \
	--go_opt=module=github.com/cid-yoon/grpc/productinfo/go/server/ecommerce \
	./productinfo/proto/*.proto


proto_before_next: proto_format
	protoc \
	-I productinfo/ \
	productinfo/proto/product_info.proto \
	--go_out=plugins=grpc:go/server/ecommerce



proto_format:
	find ./productinfo/proto -name "*.proto"

clean:
	find ./productinfo/go/server/ecommerce -name "*.pb.go" | xargs rm


